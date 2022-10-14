package com.leverx.internship.project.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan("com.leverx.internship.project")
@Configuration
@EnableScheduling
@EnableWebMvc
public class SpringConfig {

  @Bean
  public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setMaxUploadSize(100000);
    return multipartResolver;
  }

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}