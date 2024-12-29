package com.tikelespike.gamestats.api.errors;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JWTVerificationException.class)
    protected ResponseEntity<Object> handleTokenValidationException(JWTVerificationException exception,
                                                                    WebRequest request) {
        // reuse the default error response, but set the status code to unauthorized
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), UNAUTHORIZED, request);
    }
}
