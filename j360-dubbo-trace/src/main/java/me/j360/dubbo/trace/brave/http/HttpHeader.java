package me.j360.dubbo.trace.brave.http;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * Package: me.j360.dubbo.trace.brave.http
 * User: min_xu
 * Date: 2017/4/27 下午10:23
 * 说明：
 */
@Data
@ToString
public class HttpHeader {

    private Map<String,String> header;

}
