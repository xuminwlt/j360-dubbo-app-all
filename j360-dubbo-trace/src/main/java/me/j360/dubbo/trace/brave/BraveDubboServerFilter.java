package me.j360.dubbo.trace.brave;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.github.kristofa.brave.*;
import com.github.kristofa.brave.http.BraveHttpHeaders;
import com.github.kristofa.brave.internal.Util;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Collections;

import static com.github.kristofa.brave.IdConversion.convertToLong;
import static com.google.common.base.Preconditions.checkNotNull;

@Activate(group = {Constants.PROVIDER})
public class BraveDubboServerFilter implements Filter {


    /** Creates a tracing interceptor with defaults. Use {@link #builder(Brave)} to customize. */
    public static BraveDubboServerFilter create(Brave brave) {
        return new Builder(brave).build();
    }

    public static Builder builder(Brave brave) {
        return new Builder(brave);
    }

    public static final class Builder {
        final Brave brave;

        Builder(Brave brave) { // intentionally hidden
            this.brave = Util.checkNotNull(brave, "brave");
        }

        public BraveDubboServerFilter build() {
            return new BraveDubboServerFilter(this);
        }
    }

    private final ServerRequestInterceptor serverRequestInterceptor;
    private final ServerResponseInterceptor serverResponseInterceptor;


    BraveDubboServerFilter(Builder b) { // intentionally hidden
        this.serverRequestInterceptor = b.brave.serverRequestInterceptor();
        this.serverResponseInterceptor = b.brave.serverResponseInterceptor();
    }


    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        if ("com.alibaba.dubbo.monitor.MonitorService".equals(invoker.getInterface().getName())) {
            return invoker.invoke(invocation);
        }

        RpcContext context = RpcContext.getContext();
        serverRequestInterceptor.handle(new DubboServerRequestAdapter(context, invocation));
        Result result = invoker.invoke(invocation);
        serverResponseInterceptor.handle(new DubboServerResponseAdapter(result));

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
                    return TraceData.NOT_SAMPLED;
                } else {
                    final String parentSpanId = invocation.getAttachment(BraveHttpHeaders.ParentSpanId.getName());
                    final String traceId = invocation.getAttachment(BraveHttpHeaders.TraceId.getName());
                    final String spanId = invocation.getAttachment(BraveHttpHeaders.SpanId.getName());
                    if (traceId != null && spanId != null) {
                        return TraceData.create(getSpanId(traceId, spanId, parentSpanId));
                    }
                }
            }
            return TraceData.EMPTY;
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
