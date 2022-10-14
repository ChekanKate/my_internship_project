package com.leverx.internship.project.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebConfig implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) {
    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    context.register(SpringConfig.class);

    ServletRegistration.Dynamic servlet =
        servletContext.addServlet("dispatcher", new DispatcherServlet(context));
    servlet.setLoadOnStartup(1);
    servlet.addMapping("/api/*");
  }
}