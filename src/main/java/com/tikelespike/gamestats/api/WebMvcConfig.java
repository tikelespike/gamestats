package com.tikelespike.gamestats.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures the web MVC settings for the application.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ArtificialDelayInterceptor artificialDelayInterceptor;

    /**
     * Creates a new instance of the class. This is usually done by the Spring framework, which manages the
     * configuration's lifecycle and injects the required dependencies.
     *
     * @param artificialDelayInterceptor the artificial delay interceptor
     */
    public WebMvcConfig(ArtificialDelayInterceptor artificialDelayInterceptor) {
        this.artificialDelayInterceptor = artificialDelayInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(artificialDelayInterceptor);
    }
}
