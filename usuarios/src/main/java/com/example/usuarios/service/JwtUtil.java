package com.example.usuarios.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    
    // Usar variables de entorno para la clave secreta
    @Value("${jwt.secret:defaultSecretKeyForDevelopmentOnlyNotForProduction}")
    private String secret;
    
    // Token válido por 24 horas (configurable)
    @Value("${jwt.expiration:86400000}")
    private Long jwtExpiration;

    // Generar token con claims adicionales
    public String generarToken(String username, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);
        return createToken(claims, username);
    }

    // Crear token con claims personalizados
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // Obtener username del token
    public String obtenerUsername(String token) {
        return obtenerClaim(token, Claims::getSubject);
    }

    // Obtener fecha de expiración del token
    public Date obtenerFechaExpiracion(String token) {
        return obtenerClaim(token, Claims::getExpiration);
    }

    // Obtener rol del token
    public String obtenerRol(String token) {
        return obtenerClaim(token, claims -> claims.get("rol", String.class));
    }

    // Método genérico para extraer claims
    public <T> T obtenerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = obtenerTodasLasClaims(token);
        return claimsResolver.apply(claims);
    }

    // Obtener todas las claims del token
    private Claims obtenerTodasLasClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    // Validar si el token ha expirado
    public boolean esTokenExpirado(String token) {
        return obtenerFechaExpiracion(token).before(new Date());
    }

    // Validar token completo
    public boolean validarToken(String token, String username) {
        try {
            final String tokenUsername = obtenerUsername(token);
            return (tokenUsername.equals(username) && !esTokenExpirado(token));
        } catch (Exception e) {
            return false;
        }
    }

    // Validar token con manejo de excepciones detallado
    public boolean esTokenValido(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            System.err.println("JWT signature is invalid: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("JWT token is malformed: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    // Extraer token del header Authorization
    public String extraerTokenDelHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}