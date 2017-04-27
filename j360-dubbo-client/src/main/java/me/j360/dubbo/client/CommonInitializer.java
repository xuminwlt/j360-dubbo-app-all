package me.j360.dubbo.client;

/**
 * Package: me.j360.trace.example.springmvc.dubbo
 * User: min_xu
 * Date: 2016/10/12 下午6:47
 * 说明：
 */
/*@Order(2)
public class CommonInitializer implements WebApplicationInitializer {

    @Bean
    public Brave brave() {
        //default reporter LoggingSpanCollector
        Brave.Builder builder = braveBuilder(Sampler.ALWAYS_SAMPLE);
        return builder.build();
    }

    @Autowired
    private Brave brave;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        BraveServletFilter braveServletFilter = BraveServletFilter.create(brave);
        FilterRegistration.Dynamic  filterRegistration = servletContext.addFilter(
                "braveServletFilter", braveServletFilter);

        filterRegistration.addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "*//**//*");
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
}*/
