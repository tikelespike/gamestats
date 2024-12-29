package com.tikelespike.gamestats.api.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tikelespike.gamestats.businesslogic.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filters incoming requests and sets the authentication context if a valid token is present.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final TokenProvider tokenService;

    /**
     * Creates a new SecurityFilter. This is usually done by the Spring framework, which manages the filter's lifecycle
     * and injects the required dependencies.
     *
     * @param userService the authentication service to use for loading user details
     * @param tokenService the token service to use for validating tokens
     */
    public SecurityFilter(UserService userService, TokenProvider tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = this.recoverToken(request);
        if (token != null) {
            String login = null;
            try {
                login = tokenService.validateToken(token);
            } catch (JWTVerificationException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
            var user = userService.loadUserByUsername(login);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
