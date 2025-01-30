package com.tikelespike.gamestats.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Interceptor that adds an artificial delay to the request processing (before and after calling the controller). This
 * is used for testing purposes (for example, visualizing the frontend behavior when the backend is slow). The amount of
 * delay is configurable via the application properties and environment variables.
 */
@Component
public class ArtificialDelayInterceptor implements HandlerInterceptor {

    @Value("${dev.delay.after}")
    private long sleepAfterMs;

    @Value("${dev.delay.before}")
    private long sleepBeforeMs;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Thread.sleep(sleepBeforeMs);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        Thread.sleep(sleepAfterMs);
    }
}
