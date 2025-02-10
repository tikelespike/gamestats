package com.tikelespike.gamestats.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tikelespike.gamestats.businesslogic.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

/**
 * Service that provides methods to generate and validate JSON Web Tokens, used for authentication.
 */
@Service
public class TokenProvider {
    private static final Duration TOKEN_VALIDITY_DURATION = Duration.ofHours(24);

    @Value("${security.jwt.token.secret-key}")
    private String jwtSecret;

    /**
     * @param user the user for which to generate a token
     *
     * @return the generated token in string form
     */
    public String generateAccessToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("username", user.getUsername())
                    .withExpiresAt(genAccessExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while generating token", exception);
        }
    }

    /**
     * Validates a token and returns the username it was issued for.
     *
     * @param token the token to validate
     *
     * @return the username the token was issued for
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error while validating token", exception);
        }
    }

    private Instant genAccessExpirationDate() {
        return Instant.now().plus(TOKEN_VALIDITY_DURATION);
    }
}
