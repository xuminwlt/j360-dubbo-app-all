package me.j360.dubbo.servlet3.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.util.UrlPathHelper;


/**
 * equals extends WebMvcConfigurerAdapter
 *
 */
@Configuration
@EnableWebMvc
public class WebApplicationBootstrap extends WebMvcConfigurerAdapter {


    /**
     mvc:view-resolvers>
     <mvc:content-negotiation>
     <mvc:default-views>
     <bean class="org.springframework.web.servlet.view.json.MappingJackson2JsonView"/>
     </mvc:default-views>
     </mvc:content-negotiation>
     <mvc:jsp/>
     </mvc:view-resolvers>
     * @param registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.enableContentNegotiation(new MappingJackson2JsonView());
        registry.jsp();
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/public", "classpath:/static/")
                .setCachePeriod(31556926);
    }


    /**
     * <mvc:default-servlet-handler/>
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                .setUseSuffixPatternMatch(true)
                .setUseTrailingSlashMatch(false)
                .setUseRegisteredSuffixPatternMatch(true)
                .setPathMatcher(antPathMatcher())
                .setUrlPathHelper(urlPathHelper());
    }

    @Bean
    public UrlPathHelper urlPathHelper() {
        //...
        return null;
    }

    @Bean
    public PathMatcher antPathMatcher() {
        //...
        return null;
    }




}
