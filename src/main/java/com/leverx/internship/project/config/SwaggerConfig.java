package com.leverx.internship.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    org.springdoc.core.SpringDocConfigProperties.class,
    org.springdoc.core.SpringDocConfiguration.class,
    org.springdoc.webmvc.core.SpringDocWebMvcConfiguration.class,
    org.springdoc.webmvc.core.MultipleOpenApiSupportConfiguration.class,
    org.springdoc.core.SwaggerUiConfigProperties.class,
    org.springdoc.core.SwaggerUiOAuthProperties.class,
    org.springdoc.webmvc.ui.SwaggerConfig.class,
    org.springdoc.core.CacheOrGroupedOpenApiCondition.class,
    org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class
})
public class SwaggerConfig {}