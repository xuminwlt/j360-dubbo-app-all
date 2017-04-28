package me.j360.dubbo.trace.brave;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.github.kristofa.brave.*;
import com.github.kristofa.brave.http.BraveHttpHeaders;
import com.google.common.base.Objects;
import com.twitter.zipkin.gen.Span;
import org.slf4j.MDC;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Collections;

import static com.github.kristofa.brave.IdConversion.convertToLong;
import static com.github.kristofa.brave.IdConversion.convertToString;
import static com.google.common.base.Preconditions.checkNotNull;
import static me.j360.dubbo.trace.brave.DubboKeys.ZIPKIN_PARENTID_MDC;
import static me.j360.dubbo.trace.brave.DubboKeys.ZIPKIN_SPANID_MDC;
import static me.j360.dubbo.trace.brave.DubboKeys.ZIPKIN_TRACEID_MDC;

@Activate(group = {Constants.PROVIDER})
public class BraveDubboServerFilter implements Filter {


    private ServerRequestInterceptor serverRequestInterceptor;
    private ServerResponseInterceptor serverResponseInterceptor;
    private Brave brave;
    private ServerSpanThreadBinder serverSpanThreadBinder;

    public void setBrave(Brave brave) {
        this.brave = brave;
        this.serverRequestInterceptor = checkNotNull(brave.serverRequestInterceptor());
        this.serverResponseInterceptor = checkNotNull(brave.serverResponseInterceptor());
        this.serverSpanThreadBinder = brave.serverSpanThreadBinder();
    }


    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if ("com.alibaba.dubbo.monitor.MonitorService".equals(invoker.getInterface().getName())) {
            return invoker.invoke(invocation);
        }

        RpcContext context = RpcContext.getContext();
        //Request中的spanId是上一个Span的id,当前的spanId在后面会重新join一次,所以此处添加spanId理解不对
        serverRequestInterceptor.handle(new DubboServerRequestAdapter(context, invocation));

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

        Result result = invoker.invoke(invocation);
        serverResponseInterceptor.handle(new DubboServerResponseAdapter(result));

        MDC.remove(ZIPKIN_TRACEID_MDC);
        MDC.remove(ZIPKIN_SPANID_MDC);
        MDC.remove(ZIPKIN_PARENTID_MDC);

        return result;
    }


    static final class DubboServerRequestAdapter implements ServerRequestAdapter {

        private RpcContext context;
        private Invocation invocation;

        public DubboServerRequestAdapter(RpcContext context,Invocation invocation) {
            this.context = checkNotNull(context);
            this.invocation = checkNotNull(invocation);
        }

        @Override
        public TraceData getTraceData() {
            final String  sampled = invocation.getAttachment(BraveHttpHeaders.Sampled.getName());
            if (sampled != null) {
                if (sampled.equals("0") || sampled.toLowerCase().equals("false")) {
                    return TraceData.builder().sample(false).build();
                } else {
                    final String parentSpanId = invocation.getAttachment(BraveHttpHeaders.ParentSpanId.getName());
                    final String traceId = invocation.getAttachment(BraveHttpHeaders.TraceId.getName());
                    final String spanId = invocation.getAttachment(BraveHttpHeaders.SpanId.getName());
                    if (traceId != null && spanId != null) {
                        SpanId span = getSpanId(traceId, spanId, parentSpanId);
                        return TraceData.builder().sample(true).spanId(span).build();
                    }
                }
            }
            return TraceData.builder().build();
        }

        @Override
        public String getSpanName() {
            return context.getMethodName();
        }

        @Override
        public Collection<KeyValueAnnotation> requestAnnotations() {

            SocketAddress socketAddress = context.getRemoteAddress();
            if (socketAddress != null) {
                KeyValueAnnotation remoteAddrAnnotation = KeyValueAnnotation.create(
                        DubboKeys.DUBBO_REMOTE_ADDR, socketAddress.toString());
                return Collections.singleton(remoteAddrAnnotation);
            } else {
                return Collections.emptyList();
            }
        }
    }

    static final class DubboServerResponseAdapter implements ServerResponseAdapter {

        private final Result result;

        public DubboServerResponseAdapter(Result result) {
            this.result = result;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Collection<KeyValueAnnotation> responseAnnotations() {
            return result.getException() == null
                    ? Collections.<KeyValueAnnotation>emptyList()
                    : Collections.singletonList(KeyValueAnnotation.create(DubboKeys.DUBBO_EXCEPTION_NAME, result.getException().getMessage()));

        }

    }

    static SpanId getSpanId(String traceId, String spanId, String parentSpanId) {
        return SpanId.builder()
                .traceId(convertToLong(traceId))
                .spanId(convertToLong(spanId))
                .parentId(parentSpanId == null ? null : convertToLong(parentSpanId)).build();
    }

}
