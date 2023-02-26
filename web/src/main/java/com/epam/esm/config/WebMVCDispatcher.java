package com.epam.esm.config;

import lombok.NonNull;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletRegistration;

public class WebMVCDispatcher extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { SpringConfig.class };
    }

    @Override
    protected String @NonNull [] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
        super.customizeRegistration(registration);
    }
}
