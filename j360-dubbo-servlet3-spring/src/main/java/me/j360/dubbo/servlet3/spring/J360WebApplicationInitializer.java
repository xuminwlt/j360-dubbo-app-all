package me.j360.dubbo.servlet3.spring;

/**
 * @author: min_xu
 * @date: 2018/6/12 下午5:19
 * 说明：init with interface
 */

/*@Deprecated
public class J360WebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // create root context
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        ac.register(WebApplicationConfiguration.class);
        ac.refresh();

        // Create servlet Spring Context
        AnnotationConfigWebApplicationContext servletSpringContext = new AnnotationConfigWebApplicationContext();
        servletSpringContext.register(WebApplicationBootstrap.class);
        servletSpringContext.setParent(ac);

        // Create and register the DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet(servletSpringContext);
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcherServlet", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("*//*");


    }


}*/
