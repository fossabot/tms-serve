package com.odakota.tms.system.config;

import com.odakota.tms.constant.FieldConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.UUID;

/**
 * @author haidv
 * @version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.odakota.tms.business"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(securityScheme()))
                .globalOperationParameters(Collections.singletonList(parameter()))
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("REST API")
                                   .description("TMS APPLICATION.").termsOfServiceUrl("")
                                   .contact(new Contact("Đoàn Hải", "", "doanhai8080@gmail.com"))
                                   .license("Apache License Version 2.0")
                                   .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                                   .version("1.0")
                                   .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                              .securityReferences(Collections.singletonList(securityReference()))
                              .build();
    }

    private SecurityScheme securityScheme() {
        return new ApiKey(FieldConstant.API_KEY, "Authorization", "Header");
    }

    private SecurityReference securityReference() {
        return new SecurityReference(FieldConstant.API_KEY, new AuthorizationScope[0]);
    }

    private Parameter parameter() {
        return new ParameterBuilder().name("X-Request-ID")
                                     .defaultValue(UUID.randomUUID().toString())
                                     .parameterType("header")
                                     .modelRef(new ModelRef("string")).required(true).build();

    }
}
