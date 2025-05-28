package com.example.backendredu.auth;

import io.jsonwebtoken.Jwts;
import com.example.backendredu.usuario.domain.Usuario;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    // ✅ Usa el email como "subject"
    public String generateToken(Usuario user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // Aquí el cambio clave
                .claim("role", user.getUserType().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Esto ahora devuelve el email del usuario
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // ← Este subject es el email
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Este será el email
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}