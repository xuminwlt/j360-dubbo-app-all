package me.j360.dubbo.trace.brave;


import com.twitter.zipkin.gen.BinaryAnnotation;

/** Well-known {@link BinaryAnnotation#key binary annotation keys} for dubbo */
public final class DubboKeys {

    /**
     * The remote address of the client
     */
    public static final String DUBBO_REMOTE_ADDR = "dubbo.remote_addr";


    public static final String DUBBO_EXCEPTION_NAME = "dubbo.exception_name";

    private DubboKeys() {
    }
}

