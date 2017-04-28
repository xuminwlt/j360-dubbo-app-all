package me.j360.dubbo.trace.brave;


import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.github.kristofa.brave.*;
import com.github.kristofa.brave.http.BraveHttpHeaders;
import com.github.kristofa.brave.internal.Nullable;
import com.twitter.zipkin.gen.Endpoint;
import org.slf4j.MDC;

import java.util.Collection;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;
import static me.j360.dubbo.trace.brave.DubboKeys.ZIPKIN_PARENTID_MDC;
import static me.j360.dubbo.trace.brave.DubboKeys.ZIPKIN_SPANID_MDC;
import static me.j360.dubbo.trace.brave.DubboKeys.ZIPKIN_TRACEID_MDC;
@Activate(group = {Constants.CONSUMER})
public class BraveDubboClientFilter implements Filter {

    private ClientRequestInterceptor clientRequestInterceptor;
    private ClientResponseInterceptor clientResponseInterceptor;
    private Brave brave;

    //Dubbo Filter定义只能使用无参构造加Set注入形式,所以这里没用使用Brave官方推荐的builder方式生成

    public void setBrave(Brave brave) {
        this.brave = brave;
        this.clientRequestInterceptor = checkNotNull(brave.clientRequestInterceptor());
        this.clientResponseInterceptor = checkNotNull(brave.clientResponseInterceptor());
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if ("com.alibaba.dubbo.monitor.MonitorService".equals(invoker.getInterface().getName())) {
            return invoker.invoke(invocation);
        }
        RpcContext context = RpcContext.getContext();
        clientRequestInterceptor.handle(new DubboClientRequestAdapter(context, invocation));
        Result result = invoker.invoke(invocation);
        clientResponseInterceptor.handle(new DubboClientResponseAdapter(result));

        MDC.remove(ZIPKIN_TRACEID_MDC);
        MDC.remove(ZIPKIN_SPANID_MDC);
        MDC.remove(ZIPKIN_PARENTID_MDC);

        return result;
    }


    static final class DubboClientRequestAdapter implements ClientRequestAdapter {

        private final RpcContext context;
        private final Invocation invocation;

        public DubboClientRequestAdapter(RpcContext context,Invocation invocation) {
            this.context = checkNotNull(context);
            this.invocation = checkNotNull(invocation);
        }

        @Override
        public String getSpanName() {
            return context.getMethodName();
        }

        @Override
        public void addSpanIdToRequest(@Nullable SpanId spanId) {

            // 添加下游信息
            if (spanId == null) {
                ((RpcInvocation) invocation).setAttachment(BraveHttpHeaders.Sampled.getName(), "0");
            } else {
                ((RpcInvocation) invocation).setAttachment(BraveHttpHeaders.Sampled.getName(), "1");
                ((RpcInvocation) invocation).setAttachment(BraveHttpHeaders.TraceId.getName(), IdConversion.convertToString(spanId.traceId));
                ((RpcInvocation) invocation).setAttachment(BraveHttpHeaders.SpanId.getName(), IdConversion.convertToString(spanId.spanId));

                MDC.put(ZIPKIN_TRACEID_MDC, IdConversion.convertToString(spanId.traceId));
                MDC.put(ZIPKIN_SPANID_MDC, IdConversion.convertToString(spanId.spanId));

                if (spanId.nullableParentId() != null) {
                    ((RpcInvocation) invocation).setAttachment(BraveHttpHeaders.ParentSpanId.getName(), IdConversion.convertToString(spanId.parentId));

                    MDC.put(ZIPKIN_PARENTID_MDC, IdConversion.convertToString(spanId.parentId));
                } else {
                    MDC.remove(ZIPKIN_PARENTID_MDC);
                }

            }
        }

        @Override
        public Collection<KeyValueAnnotation> requestAnnotations() {
            return Collections.emptyList();
        }

        @Override
        public Endpoint serverAddress() {
            return null;
        }
    }

    static final class DubboClientResponseAdapter implements ClientResponseAdapter {

        private final Result result;

        public DubboClientResponseAdapter(Result result) {
            this.result = checkNotNull(result);
        }

        @Override
        public Collection<KeyValueAnnotation> responseAnnotations() {
            return result.getException() == null
                    ? Collections.<KeyValueAnnotation>emptyList()
                    : Collections.singletonList(KeyValueAnnotation.create(DubboKeys.DUBBO_EXCEPTION_NAME, result.getException().getMessage()));
        }
    }

}
