package me.j360.dubbo.trace.brave;


import com.twitter.zipkin.gen.BinaryAnnotation;

/** Well-known {@link BinaryAnnotation#key binary annotation keys} for dubbo */
public final class DubboKeys {

    /**
     * The remote address of the client
     */
    public static final String DUBBO_REMOTE_ADDR = "dubbo.remote_addr";


    public static final String DUBBO_EXCEPTION_NAME = "dubbo.exception_name";

    public static final String ZIPKIN_TRACEID_MDC = "zipkinTraceId";
    public static final String ZIPKIN_SPANID_MDC = "zipkinSpanId";
    public static final String ZIPKIN_PARENTID_MDC = "zipkinParentId";

    public static final String HTTP_HEADERS = "http.headers";
    public static final String HTTP_PARAMS = "http.params";
    public static final String HTTP_METHOD = "http.method";

    private DubboKeys() {
    }
}

