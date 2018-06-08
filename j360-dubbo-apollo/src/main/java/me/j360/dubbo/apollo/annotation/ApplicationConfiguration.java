package me.j360.dubbo.apollo.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Package: cn.paomaintv.user.configuration
 * User: min_xu
 * Date: 2017/6/12 下午7:59
 * 说明：
 */

@Configuration
@ImportResource({
        "META-INF/spring/application-service.xml",
        "META-INF/spring/application-dal.xml"})
public class ApplicationConfiguration {


}
