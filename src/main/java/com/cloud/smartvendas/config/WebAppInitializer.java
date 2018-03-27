package com.cloud.smartvendas.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
 
public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        AnnotationConfigWebApplicationContext mvcContext = 
                new AnnotationConfigWebApplicationContext();
            mvcContext.register(SpringConfigMvc.class);
            
            ServletRegistration.Dynamic dispatcher = container.addServlet("servletDispatcher", 
                new DispatcherServlet(mvcContext));
            dispatcher.setLoadOnStartup(1);
            dispatcher.addMapping("/");
    }
 }