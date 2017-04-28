package me.j360.dubbo.trace.brave.http;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.kristofa.brave.*;
import com.github.kristofa.brave.http.HttpServerRequestAdapter;
import com.github.kristofa.brave.http.SpanNameProvider;
import com.github.kristofa.brave.internal.Nullable;
import com.github.kristofa.brave.servlet.ServletHttpServerRequest;
import com.github.kristofa.brave.servlet.internal.MaybeAddClientAddressFromRequest;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.twitter.zipkin.gen.Span;
import me.j360.dubbo.trace.brave.DubboKeys;
import me.j360.dubbo.trace.brave.util.AntPathMatcher;
import me.j360.dubbo.trace.brave.util.PatternMatcher;
import me.j360.dubbo.trace.brave.util.WebUtils;
import org.slf4j.MDC;
import zipkin.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.*;

import static com.github.kristofa.brave.IdConversion.convertToString;
import static com.github.kristofa.brave.internal.Util.checkNotNull;
import static me.j360.dubbo.trace.brave.DubboKeys.*;

/**
 * Package: me.j360.dubbo.trace.brave.http
 * User: min_xu
 * Date: 2017/4/27 下午7:59
 * 说明：
 */
public class HttpBraveServletFilter implements Filter {

    /** Creates a tracing filter with defaults. Use {@link #builder(Brave,Set)} to customize. */
    public static HttpBraveServletFilter create(Brave brave,Set paths) {
        return new Builder(brave,paths).build();
    }

    public static Builder builder(Brave brave,Set paths) {
        return new Builder(brave,paths);
    }

    /**
     * PatternMatcher used in determining which paths to react to for a given request.
     */
    protected PatternMatcher pathMatcher = new AntPathMatcher();
    protected Set<String> appliedPaths = new HashSet<String>();

    private boolean isFilterTraced = false;

    public static final class Builder {
        final Brave brave;
        final Set paths;

        //提供半路径作为spanName
        SpanNameProvider spanNameProvider = new HttpSpanNameProvider();

        Builder(Brave brave,Set paths) { // intentionally hidden
            this.paths = checkNotNull(paths, "paths");
            this.brave = checkNotNull(brave, "brave");
        }

        public Builder spanNameProvider(SpanNameProvider spanNameProvider) {
            this.spanNameProvider = checkNotNull(spanNameProvider, "spanNameProvider");
            return this;
        }

        public HttpBraveServletFilter build() {
            return new HttpBraveServletFilter(this,paths);
        }
    }

    private final ServerRequestInterceptor requestInterceptor;
    private final ServerResponseInterceptor responseInterceptor;
    private final SpanNameProvider spanNameProvider;
    @Nullable // while deprecated constructor is in use
    private final ServerTracer serverTracer;
    private final MaybeAddClientAddressFromRequest maybeAddClientAddressFromRequest;
    private final MaybeAddClientHeaderParamFromRequest maybeAddClientHeaderParamFromRequest;
    private final ServerSpanThreadBinder serverSpanThreadBinder;

    private FilterConfig filterConfig;

    protected HttpBraveServletFilter(Builder b,Set paths) { // intentionally hidden
        this.requestInterceptor = b.brave.serverRequestInterceptor();
        this.responseInterceptor = b.brave.serverResponseInterceptor();
        this.spanNameProvider = b.spanNameProvider;
        this.serverTracer = b.brave.serverTracer();
        this.maybeAddClientAddressFromRequest = MaybeAddClientAddressFromRequest.create(b.brave);
        this.maybeAddClientHeaderParamFromRequest = MaybeAddClientHeaderParamFromRequest.create(b.brave);
        this.serverSpanThreadBinder = b.brave.serverSpanThreadBinder();
        this.appliedPaths = paths;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        if (this.appliedPaths == null || this.appliedPaths.isEmpty()) {
            return true;
        }

        for (String path : this.appliedPaths) {
            // If the path does match, then pass on to the subclass implementation for specific checks
            //(first match 'wins'):
            if (pathsMatch(path, request)) {
                isFilterTraced = true;
                return true;
            }
        }

        //no path matched, allow the request to go through:
        return true;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //如果不在范围内则直接排除
        if (isFilterTraced==false) {
            filterChain.doFilter(request, response);
        }

        String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
        boolean hasAlreadyFilteredAttribute = request.getAttribute(alreadyFilteredAttributeName) != null;

        if (hasAlreadyFilteredAttribute) {
            // Proceed without invoking this filter...
            filterChain.doFilter(request, response);
        } else {
            request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            final Servlet25ServerResponseAdapter
                    servlet25ServerResponseAdapter = new Servlet25ServerResponseAdapter((HttpServletResponse) response);

            //Request中的spanId是上一个Span的id,当前的spanId在后面会重新join一次,所以此处添加spanId理解不对
            requestInterceptor.handle(new HttpServerRequestAdapter(new ServletHttpServerRequest(httpRequest), spanNameProvider));

            //ServerTrace Add to MDC
            ServerSpan serverSpan = serverSpanThreadBinder.getCurrentServerSpan();
            if(Objects.equal(Boolean.TRUE,serverSpan.getSample())){
                Span span = serverSpan.getSpan();
                MDC.put(ZIPKIN_TRACEID_MDC, convertToString(span.getTrace_id()));
                MDC.put(ZIPKIN_SPANID_MDC, convertToString(span.getId()));
                if (span.getParent_id() != null) {
                    MDC.put(ZIPKIN_PARENTID_MDC, convertToString(span.getParent_id()));
                } else {
                    MDC.remove(ZIPKIN_PARENTID_MDC);
                }
            }

            if (maybeAddClientAddressFromRequest != null) {
                maybeAddClientAddressFromRequest.accept(httpRequest);
            }

            //TODO: add common binaryAnnotation,或者使用key=headers value="a=b,b=c,c=d"

            if (maybeAddClientHeaderParamFromRequest != null) {
                maybeAddClientHeaderParamFromRequest.accept(serverTracer,httpRequest);
            }

            if (serverTracer != null) {
                serverTracer.submitBinaryAnnotation(DubboKeys.HTTP_METHOD, ((HttpServletRequest) request).getMethod());
            }

            try {
                filterChain.doFilter(request, servlet25ServerResponseAdapter);
            } catch (IOException | ServletException| RuntimeException | Error e) {
                if (serverTracer != null) {
                    // TODO: revisit https://github.com/openzipkin/openzipkin.github.io/issues/52
                    String message = e.getMessage();
                    if (message == null) message = e.getClass().getSimpleName();
                    serverTracer.submitBinaryAnnotation(Constants.ERROR, message);
                }
                throw e;
            } finally {
                responseInterceptor.handle(servlet25ServerResponseAdapter);

                MDC.remove(ZIPKIN_TRACEID_MDC);
                MDC.remove(ZIPKIN_SPANID_MDC);
                MDC.remove(ZIPKIN_PARENTID_MDC);
            }
        }
    }

    @Override
    public void destroy() {

    }

    private String getAlreadyFilteredAttributeName() {
        String name = getFilterName();
        if (name == null) {
            name = getClass().getName();
        }
        return name + ".FILTERED";
    }

    private final String getFilterName() {
        return (this.filterConfig != null ? this.filterConfig.getFilterName() : null);
    }

    /** When deployed in Servlet 2.5 environment {@link #getStatus} is not available. */
    static final class Servlet25ServerResponseAdapter extends HttpServletResponseWrapper implements
            ServerResponseAdapter {
        // The Servlet spec says: calling setStatus is optional, if no status is set, the default is OK.
        int httpStatus = HttpServletResponse.SC_OK;

        Servlet25ServerResponseAdapter(HttpServletResponse response) {
            super(response);
        }

        @Override public void setStatus(int sc, String sm) {
            httpStatus = sc;
            super.setStatus(sc, sm);
        }

        @Override public void sendError(int sc) throws IOException {
            httpStatus = sc;
            super.sendError(sc);
        }

        @Override public void sendError(int sc, String msg) throws IOException {
            httpStatus = sc;
            super.sendError(sc, msg);
        }

        @Override public void setStatus(int sc) {
            httpStatus = sc;
            super.setStatus(sc);
        }


        /** Alternative to {@link #getStatus}, but for Servlet 2.5+ */
        @Override public Collection<KeyValueAnnotation> responseAnnotations() {
            String status = getHeader("status");
            String message = getHeader("message");

            if(StringUtils.isNotEmpty(status)) {
                if (Integer.parseInt(status) != 0) {
                    KeyValueAnnotation keyValueAnnotation1 = KeyValueAnnotation.create(DubboKeys.HTTP_STATUS, status);
                    KeyValueAnnotation keyValueAnnotation2 = KeyValueAnnotation.create(DubboKeys.HTTP_MESSAGE, message);
                    return Lists.newArrayList(keyValueAnnotation1 ,keyValueAnnotation2);
                }
            }
            return Collections.singleton(
                    KeyValueAnnotation.create(zipkin.TraceKeys.HTTP_STATUS_CODE, String.valueOf(httpStatus))
            );
        }
    }


    protected String getPathWithinApplication(ServletRequest request) {
        return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
    }

    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = getPathWithinApplication(request);
        //log.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, requestURI);
        return pathsMatch(path, requestURI);
    }


    protected boolean pathsMatch(String pattern, String path) {
        return pathMatcher.matches(pattern, path);
    }


}
