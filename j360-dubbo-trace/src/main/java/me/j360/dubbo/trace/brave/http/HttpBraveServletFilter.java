package me.j360.dubbo.trace.brave.http;

import com.github.kristofa.brave.*;
import com.github.kristofa.brave.http.HttpServerRequestAdapter;
import com.github.kristofa.brave.http.SpanNameProvider;
import com.github.kristofa.brave.internal.Nullable;
import com.github.kristofa.brave.servlet.ServletHttpServerRequest;
import com.github.kristofa.brave.servlet.internal.MaybeAddClientAddressFromRequest;
import me.j360.dubbo.trace.brave.DubboKeys;
import zipkin.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static com.github.kristofa.brave.internal.Util.checkNotNull;

/**
 * Package: me.j360.dubbo.trace.brave.http
 * User: min_xu
 * Date: 2017/4/27 下午7:59
 * 说明：
 */
public class HttpBraveServletFilter implements Filter {

    /** Creates a tracing filter with defaults. Use {@link #builder(Brave)} to customize. */
    public static HttpBraveServletFilter create(Brave brave) {
        return new Builder(brave).build();
    }

    public static Builder builder(Brave brave) {
        return new Builder(brave);
    }

    public static final class Builder {
        final Brave brave;

        //提供半路径作为spanName
        SpanNameProvider spanNameProvider = new HttpSpanNameProvider();

        Builder(Brave brave) { // intentionally hidden
            this.brave = checkNotNull(brave, "brave");
        }

        public Builder spanNameProvider(SpanNameProvider spanNameProvider) {
            this.spanNameProvider = checkNotNull(spanNameProvider, "spanNameProvider");
            return this;
        }

        public HttpBraveServletFilter build() {
            return new HttpBraveServletFilter(this);
        }
    }

    private final ServerRequestInterceptor requestInterceptor;
    private final ServerResponseInterceptor responseInterceptor;
    private final SpanNameProvider spanNameProvider;
    @Nullable // while deprecated constructor is in use
    private final ServerTracer serverTracer;
    private final MaybeAddClientAddressFromRequest maybeAddClientAddressFromRequest;

    private FilterConfig filterConfig;

    protected HttpBraveServletFilter(Builder b) { // intentionally hidden
        this.requestInterceptor = b.brave.serverRequestInterceptor();
        this.responseInterceptor = b.brave.serverResponseInterceptor();
        this.spanNameProvider = b.spanNameProvider;
        this.serverTracer = b.brave.serverTracer();
        this.maybeAddClientAddressFromRequest = MaybeAddClientAddressFromRequest.create(b.brave);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

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
            requestInterceptor.handle(new HttpServerRequestAdapter(new ServletHttpServerRequest(httpRequest), spanNameProvider));

            if (maybeAddClientAddressFromRequest != null) {
                maybeAddClientAddressFromRequest.accept(httpRequest);
            }

            //add common binaryAnnotation
            if (serverTracer != null) {
                serverTracer.submitBinaryAnnotation(DubboKeys.HTTP_CLIENT_AGENT, ((HttpServletRequest) request).getHeader("clientAgent"));
                serverTracer.submitBinaryAnnotation(DubboKeys.HTTP_CLIENT_AGENT, ((HttpServletRequest) request).getHeader("clientAgent"));
                serverTracer.submitBinaryAnnotation(DubboKeys.HTTP_CLIENT_AGENT, ((HttpServletRequest) request).getHeader("clientAgent"));
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
            return Collections.singleton(
                    KeyValueAnnotation.create(zipkin.TraceKeys.HTTP_STATUS_CODE, String.valueOf(httpStatus))
            );
        }
    }

}
