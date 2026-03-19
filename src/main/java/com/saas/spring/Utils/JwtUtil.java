package com.saas.spring.Utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.saas.spring.Role.Role;
import com.saas.spring.User.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey(){
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user){
        return buildToken(user);
    }

    public String refreshToken(User user){
        return buildToken(user);
    }

    public Long extractUserId(String token) {
    return Long.parseLong(parseClaims(token).getSubject());
    }

    public boolean  isTokenValid(String token, User user){
        Long id = extractUserId(token);
        return (id.equals(user.getId()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token){
        Date expirationDate = parseClaims(token).getExpiration();
        return expirationDate == null || expirationDate.before(new Date());
    }
    
    private Claims parseClaims(String token){
        return Jwts.parserBuilder().build()
                    .parseClaimsJws(token).getBody();
    }

    private String buildToken(User user){
        Map<String, Object> claims = new HashMap<>();
        //Brecha de seguridad
        claims.put("Nombre", user.getNombre());
        claims.put("Role", user.getRoles().stream().map(Role::getNombre).toList());

        Date now = new Date();
        Date expiry = new Date(now.getTime()+jwtExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}
