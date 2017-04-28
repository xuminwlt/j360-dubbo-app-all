package me.j360.dubbo.trace.brave.http;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.kristofa.brave.AnnotationSubmitter;
import com.github.kristofa.brave.Brave;
import me.j360.dubbo.trace.brave.DubboKeys;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Package: me.j360.dubbo.trace.brave.http
 * User: min_xu
 * Date: 2017/4/27 下午8:16
 * 说明：
 */
public final class MaybeAddClientHeaderParamFromRequest {

    final Brave brave;

    public static MaybeAddClientHeaderParamFromRequest create(Brave brave) {
        return new MaybeAddClientHeaderParamFromRequest(brave);
    }

    MaybeAddClientHeaderParamFromRequest(Brave brave) { // intentionally hidden
        this.brave = brave;
    }


    public final void accept(AnnotationSubmitter annotationSubmitter, HttpServletRequest input) {
        if (annotationSubmitter != null) {
            annotationSubmitter.submitBinaryAnnotation(DubboKeys.HTTP_HEADERS,buildHeader(input).toString());
            annotationSubmitter.submitBinaryAnnotation(DubboKeys.HTTP_PARAMS,buildParam(input).toString());
        }
    }

    private HttpHeader buildHeader(HttpServletRequest request) {
        HttpHeader header = new HttpHeader();

        //headers
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<String, String>();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
        }

        if (headers.containsKey("timestamp")) {
            headers.put("client-timestamp", headers.get("timestamp"));
        }else {
            headers.put("client-timestamp","0");
        }
        headers.put("timestamp", System.currentTimeMillis() + "");

        header.setHeader(headers);
        return header;
    }

    private HttpParam buildParam(HttpServletRequest request) {
        HttpParam param = new HttpParam();

        //params
        Map<String, String[]> queryParamsTemp = request.getParameterMap();
        Map<String, String> queryParams = new HashMap<String, String>(queryParamsTemp.size());
        for (Map.Entry<String, String[]> entry : queryParamsTemp.entrySet()) {
            String key = entry.getKey();
            String[] valueTemp = entry.getValue();
            String value = StringUtils.join(valueTemp,",");
            queryParams.put(key, value);
        }
        param.setParam(queryParams);
        return param;
    }
}
