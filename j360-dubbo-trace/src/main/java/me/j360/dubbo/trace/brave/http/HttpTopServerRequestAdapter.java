package me.j360.dubbo.trace.brave.http;

import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.TraceData;
import com.github.kristofa.brave.http.BraveHttpHeaders;
import com.github.kristofa.brave.http.HttpServerRequest;
import com.github.kristofa.brave.http.HttpServerRequestAdapter;
import com.github.kristofa.brave.http.SpanNameProvider;
import org.slf4j.MDC;

import static com.github.kristofa.brave.IdConversion.convertToLong;
import static me.j360.dubbo.trace.brave.DubboKeys.ZIPKIN_PARENTID_MDC;
import static me.j360.dubbo.trace.brave.DubboKeys.ZIPKIN_SPANID_MDC;
import static me.j360.dubbo.trace.brave.DubboKeys.ZIPKIN_TRACEID_MDC;

/**
 * Package: me.j360.dubbo.trace.brave.http
 * User: min_xu
 * Date: 2017/4/28 下午1:06
 * 说明：
 */
public class HttpTopServerRequestAdapter extends HttpServerRequestAdapter {

    private final HttpServerRequest request;
    private final SpanNameProvider spanNameProvider;

    public HttpTopServerRequestAdapter(HttpServerRequest request, SpanNameProvider spanNameProvider) {
        super(request, spanNameProvider);

        this.request = request;
        this.spanNameProvider = spanNameProvider;
    }

    @Override
    public TraceData getTraceData() {
        String sampled = request.getHttpHeaderValue(BraveHttpHeaders.Sampled.getName());
        String parentSpanId = request.getHttpHeaderValue(BraveHttpHeaders.ParentSpanId.getName());
        String traceId = request.getHttpHeaderValue(BraveHttpHeaders.TraceId.getName());
        String spanId = request.getHttpHeaderValue(BraveHttpHeaders.SpanId.getName());

        // Official sampled value is 1, though some old instrumentation send true
        Boolean parsedSampled = sampled != null
                ? sampled.equals("1") || sampled.equalsIgnoreCase("true")
                : null;

        MDC.put(ZIPKIN_TRACEID_MDC, traceId);
        MDC.put(ZIPKIN_SPANID_MDC, spanId);

        if (parentSpanId != null) {
            MDC.put(ZIPKIN_PARENTID_MDC, parentSpanId);
        } else {
            MDC.remove(ZIPKIN_PARENTID_MDC);
        }

        if (traceId != null && spanId != null) {
            return TraceData.create(getSpanId(traceId, spanId, parentSpanId, parsedSampled));
        } else if (parsedSampled == null) {
            return TraceData.EMPTY;
        } else if (parsedSampled.booleanValue()) {
            // Invalid: The caller requests the trace to be sampled, but didn't pass IDs
            return TraceData.EMPTY;
        } else {
            return TraceData.NOT_SAMPLED;
        }
    }

    static SpanId getSpanId(String traceId, String spanId, String parentSpanId, Boolean sampled) {
        return SpanId.builder()
                .traceIdHigh(traceId.length() == 32 ? convertToLong(traceId, 0) : 0)
                .traceId(convertToLong(traceId))
                .spanId(convertToLong(spanId))
                .sampled(sampled)
                .parentId(parentSpanId == null ? null : convertToLong(parentSpanId)).build();
    }
}
