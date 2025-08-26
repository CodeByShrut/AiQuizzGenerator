package com.assignment.playpowerlabs.AIQuizzer.security;

import com.assignment.playpowerlabs.AIQuizzer.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

//    private String SECRET;
    private static final long EXPIRATION_TIME_MS = 86400000L;
    private final Key SECRET_KEY;

    public JwtUtil(@Value("${jwt.secret}") String SECRET) {
        this.SECRET_KEY = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String username, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(SignatureAlgorithm.HS256, this.SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return this.getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return this.getClaims(token).get("role", String.class);
    }

     public boolean isTokenValid(String token) {
        try {
            Claims claims = this.getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception var3) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(token).getBody();
    }
}
