package me.j360.dubbo.client;

/**
 * Package: me.j360.trace.example.springmvc.dubbo
 * User: min_xu
 * Date: 2016/10/12 下午6:47
 * 说明：
 */
/*@Order(2)
public class CommonInitializer implements WebApplicationInitializer {

    @Autowired
    private Brave brave;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {


        J360ServletFilter j360ServletFilter = new J360ServletFilter(brave.serverRequestInterceptor(), brave.serverResponseInterceptor(), new DefaultSpanNameProvider());

        FilterRegistration.Dynamic  filterRegistration = servletContext.addFilter(
                "j360ServletFilter", j360ServletFilter);

        filterRegistration.addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "*//*");
    }
}*/
