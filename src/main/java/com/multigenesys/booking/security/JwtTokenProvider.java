package com.multigenesys.booking.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenProvider {

    private final String secret;
    private final long validityInMs;
    private final Algorithm algorithm;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long validityInMs
    ) {
        this.secret = secret;
        this.validityInMs = validityInMs;
        this.algorithm = Algorithm.HMAC256(secret); // init algorithm once
    }

    /**
     * Generate JWT token
     */
    public String createToken(String username, Set<String> roles, Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return JWT.create()
                .withSubject(username)
                // .withClaim("roles", roles.toArray(new String[0]))
                .withClaim("uid", userId)
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .sign(algorithm);
    }

    /**
     * Validate and decode JWT token
     */
    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}
