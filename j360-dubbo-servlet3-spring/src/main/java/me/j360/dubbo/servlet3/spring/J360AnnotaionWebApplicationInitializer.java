package me.j360.dubbo.servlet3.spring;

import me.j360.dubbo.servlet3.spring.configuration.WebApplicationBootstrap;
import me.j360.dubbo.servlet3.spring.configuration.WebApplicationConfiguration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * @author: min_xu
 * @date: 2018/6/12 下午5:19
 * 说明：init with annotation equals with webApplication
 */
public class J360AnnotaionWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { WebApplicationConfiguration.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebApplicationBootstrap.class };
    }

    //dispatch servlet
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/*" };
    }

    //filters
    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] {
                new HiddenHttpMethodFilter(), new CharacterEncodingFilter() };
    }

}
