package me.j360.dubbo.springfox;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.*;
import static springfox.documentation.builders.PathSelectors.*;

@Configuration
@EnableSwagger2
public class ApplicationSwaggerConfig {


    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().build();
                        //Ignores controllers annotated with @CustomIgnore

    }
}
