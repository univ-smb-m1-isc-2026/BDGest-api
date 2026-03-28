package com.univ_smb_m1_isc_2026.BDGest_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // ⚡ injecte depuis application.yaml ou variable d'env
    @Value("${app.jwt.secret:dev-secret-key-change-in-production}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}") // 1 jour en ms
    private long jwtExpirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Génère un token JWT pour un username
    public String generateJwtToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Récupère les claims (payload) depuis le JWT
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()                      // ⚠️ Obligatoire
                .parseClaimsJws(token)
                .getBody();
    }

    // Récupère le username depuis le JWT
    public String getUsernameFromJwt(String token) {
        return extractClaims(token).getSubject();
    }

    // Vérifie si le token est valide et non expiré
    public boolean validateJwtToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}