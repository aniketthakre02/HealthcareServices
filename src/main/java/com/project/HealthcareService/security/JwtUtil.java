package com.project.HealthcareService.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.validity}")
    private long validityMs;

    //generate token
    public String generateToken(String email,String userName,String role){
        return Jwts.builder()
                .setSubject(email)
                .claim("name", userName)
                .claim("role", role)//  adds name as a custom claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    public String extractEmail(String token) {
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean validateToken(String token, UserDetails userDetails){
       return extractEmail(token).equals(userDetails.getUsername());
    }

}
