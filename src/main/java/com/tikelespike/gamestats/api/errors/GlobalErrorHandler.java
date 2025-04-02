package com.tikelespike.gamestats.api.errors;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Global exception handler that acts as a fallback for all exceptions that are not handled by specific controllers.
 */
@ControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles JWT verification exceptions. This is used to handle cases where the JWT token is invalid or expired.
     *
     * @param exception exception resulting from a failed JWT verification
     * @param request the request that caused the exception
     *
     * @return a response entity with the error message and a 401 Unauthorized status code
     */
    @ExceptionHandler(JWTVerificationException.class)
    protected ResponseEntity<Object> handleTokenValidationException(JWTVerificationException exception,
                                                                    WebRequest request) {
        // reuse the default error response, but set the status code to unauthorized
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), UNAUTHORIZED, request);
    }
}
