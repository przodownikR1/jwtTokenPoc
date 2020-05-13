package pl.scalatech.auth.jwtsecurity.infrastucture.security.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true")
public class SwaggerConfiguration implements WebMvcConfigurer {
    private static final String WEBJARS = "/webjars/**";
    private static final String SWAGGER_UI_HTML = "swagger-ui.html";
    private static final String PACKAGE = "pl.scalatech.auth.jwtsecurity";

    @Bean
    Docket api() {
        return new Docket(SWAGGER_2).groupName("JWT POC API")
                                    .select()
                                    .apis(RequestHandlerSelectors.basePackage(PACKAGE))
                                    .apis(RequestHandlerSelectors.any())
                                    .paths(any())
                                    .build()
                                    .enable(true)
                                    .apiInfo(apiInfo())
                                    .useDefaultResponseMessages(false)
                                    .securitySchemes(Arrays.asList(apiKey()));
    }


    @Bean
    Docket actuatorApi() {
        return new Docket(SWAGGER_2).groupName("Spring Actuator")
                                    .select()
                                    .apis(RequestHandlerSelectors.basePackage("org.springframework.boot.actuate"))
                                    .paths(regex("/actuator/.*"))
                                    .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("JWT SECURITY POC")
                                   .description("Simple demonstrator of JWT usage \n\n")
                                   .termsOfServiceUrl("localhost")
                                   .version("1.0")
                                   .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("jwtToken", "Authorization", "header");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(SWAGGER_UI_HTML)
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler(WEBJARS)
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}