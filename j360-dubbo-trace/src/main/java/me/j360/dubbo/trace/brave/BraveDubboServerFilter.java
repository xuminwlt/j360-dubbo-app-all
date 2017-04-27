package me.j360.dubbo.trace.brave;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.github.kristofa.brave.*;
import com.github.kristofa.brave.http.BraveHttpHeaders;
import org.slf4j.MDC;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Collections;

import static com.github.kristofa.brave.IdConversion.convertToLong;
import static com.google.common.base.Preconditions.checkNotNull;
import static me.j360.dubbo.trace.brave.DubboKeys.ZIPKIN_PARENTID_MDC;
import static me.j360.dubbo.trace.brave.DubboKeys.ZIPKIN_SPANID_MDC;
import static me.j360.dubbo.trace.brave.DubboKeys.ZIPKIN_TRACEID_MDC;

@Activate(group = {Constants.PROVIDER})
public class BraveDubboServerFilter implements Filter {


    private ServerRequestInterceptor serverRequestInterceptor;
    private ServerResponseInterceptor serverResponseInterceptor;
    private Brave brave;

    public void setBrave(Brave brave) {
        this.brave = brave;
        this.serverRequestInterceptor = checkNotNull(brave.serverRequestInterceptor());
        this.serverResponseInterceptor = checkNotNull(brave.serverResponseInterceptor());
    }


    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        if ("com.alibaba.dubbo.monitor.MonitorService".equals(invoker.getInterface().getName())) {
            return invoker.invoke(invocation);
        }

        RpcContext context = RpcContext.getContext();
        serverRequestInterceptor.handle(new DubboServerRequestAdapter(context, invocation));
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

                        MDC.put(ZIPKIN_TRACEID_MDC, traceId);
                        MDC.put(ZIPKIN_SPANID_MDC, spanId);

                        if (parentSpanId != null) {
                            MDC.put(ZIPKIN_PARENTID_MDC, parentSpanId);
                        } else {
                            MDC.remove(ZIPKIN_PARENTID_MDC);
                        }

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
