package me.j360.dubbo.common;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.InheritableServerClientAndLocalSpanState;
import com.github.kristofa.brave.Sampler;
import me.j360.dubbo.trace.brave.Slf4jLogReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin.Endpoint;

/**
 * Package: me.j360.dubbo.common
 * User: min_xu
 * Date: 2017/4/27 下午4:19
 * 说明：
 */
@Configuration
public class BraveConfig {

    @Bean
    public Brave brave() {
        //default reporter LoggingSpanCollector
        Brave.Builder builder = braveBuilder(Sampler.ALWAYS_SAMPLE);
        return builder.build();
    }

    Endpoint local = Endpoint.builder().serviceName("local").ipv4(127 << 24 | 1).port(100).build();
    Brave.Builder braveBuilder(Sampler sampler) {
        com.twitter.zipkin.gen.Endpoint localEndpoint = com.twitter.zipkin.gen.Endpoint.builder()
                .ipv4(local.ipv4)
                .ipv6(local.ipv6)
                .port(local.port)
                .serviceName(local.serviceName)
                .build();
        return new Brave.Builder(new InheritableServerClientAndLocalSpanState(localEndpoint))
                .reporter(new Slf4jLogReporter("zipkin"))
                .traceSampler(sampler);
    }
}
