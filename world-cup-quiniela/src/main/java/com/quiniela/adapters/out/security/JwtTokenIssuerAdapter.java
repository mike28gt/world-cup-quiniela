package com.quiniela.adapters.out.security;

import com.quiniela.config.JwtProperties;
import com.quiniela.ports.out.TokenIssuer;
import com.quiniela.ports.out.TokenValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenIssuerAdapter implements TokenIssuer, TokenValidator {
    private final JwtProperties jwtProperties;
    private final SecretKey key;

    public JwtTokenIssuerAdapter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(
                jwtProperties.secret().getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String issueAccessToken(UUID userId, String email) {

        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtProperties.expirationMinutes() * 60);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(key)
                .compact();
    }

    @Override
    public TokenData validate(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        UUID userId = UUID.fromString(claims.getSubject());
        String email = claims.get("email", String.class);

        return new TokenData(userId, email);
    }
}
