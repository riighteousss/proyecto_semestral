package com.example.usuarios.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    
    // Clave secreta para firmar los tokens (debe estar en Base64 y ser segura)
    @Value("${jwt.secret:defaultSecretKeyForDevelopmentOnlyNotForProduction}")
    private String secret;
    
    // Tiempo de expiración del token en milisegundos (por defecto 24 horas)
    @Value("${jwt.expiration:86400000}")
    private Long jwtExpiration;

    // Convierte la clave secreta en Base64 a un objeto Key para firmar
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Genera un token JWT con el username y rol proporcionados
    public String generarToken(String username, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol); // Agrega el rol como claim adicional
        return createToken(claims, username);
    }

    // Crea el token JWT con los claims y subject (username) proporcionados
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims) // Claims personalizados
                .subject(subject) // Subject (normalmente el username)
                .issuedAt(new Date(System.currentTimeMillis())) // Fecha de creación
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Fecha de expiración
                .signWith(getSigningKey()) // Firma con la clave secreta
                .compact(); // Genera el token como string
    }

    // Extrae el username (subject) del token
    public String obtenerUsername(String token) {
        return obtenerClaim(token, Claims::getSubject);
    }

    // Obtiene la fecha de expiración del token
    public Date obtenerFechaExpiracion(String token) {
        return obtenerClaim(token, Claims::getExpiration);
    }

    // Obtiene el rol almacenado en los claims del token
    public String obtenerRol(String token) {
        return obtenerClaim(token, claims -> claims.get("rol", String.class));
    }

    // Método genérico para obtener cualquier claim del token
    public <T> T obtenerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = obtenerTodasLasClaims(token);
        return claimsResolver.apply(claims);
    }

    // Obtiene todos los claims del token
    private Claims obtenerTodasLasClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey()) // Configura la clave de verificación
                .build() // Construye el parser
                .parseClaimsJws(token) // Parsea el token
                .getBody(); // Obtiene los claims
    }

    // Verifica si el token ha expirado
    public boolean esTokenExpirado(String token) {
        return obtenerFechaExpiracion(token).before(new Date());
    }

    // Valida que el token corresponda al usuario y no esté expirado
    public boolean validarToken(String token, String username) {
        try {
            final String tokenUsername = obtenerUsername(token);
            return (tokenUsername.equals(username) && !esTokenExpirado(token));
        } catch (Exception e) {
            return false;
        }
    }

    // Verifica la validez general del token (firma, formato, expiración)
    public boolean esTokenValido(String token) {
        try {
            Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.err.println("Firma JWT inválida: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("Token JWT malformado: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("Token JWT expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Token JWT no soportado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string está vacío: " + e.getMessage());
        }
        return false;
    }

    // Extrae el token del encabezado Authorization (elimina "Bearer ")
    public String extraerTokenDelHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Elimina "Bearer " del inicio
        }
        return null;
    }
}