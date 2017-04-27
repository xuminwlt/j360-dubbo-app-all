package me.j360.dubbo.trace.brave.http;

import com.github.kristofa.brave.http.HttpRequest;
import com.github.kristofa.brave.http.SpanNameProvider;

/**
 * Package: me.j360.dubbo.trace.brave.http
 * User: min_xu
 * Date: 2017/4/27 下午8:06
 * 说明：
 */
public class HttpSpanNameProvider  implements SpanNameProvider {

    @Override
    public String spanName(HttpRequest request) {
        return request.getUri().getPath();
    }
}
