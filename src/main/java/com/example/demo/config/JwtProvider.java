package com.example.demo.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    private final String jwtSecret = Base64.getEncoder().encodeToString("mi-secreto-asdasd-asdasd-asdasd-asdassdd--asdaaada-aa-ghjghj-asdas-qwwwqq-bbbxxa-etyj-sxda-yyusd-sdasda".getBytes());

//    30 minutos por ahora
    private final long jwtExpiration = 1000 * 60 * 30000;

    public String generateToken(Authentication auth){
        UserDetails principalUser = (UserDetails) auth.getPrincipal();
        return Jwts.builder()
                .setSubject(principalUser.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }
    public String generateToken(String username){

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }
    public String getUsernameFromToken(String token){
        String username =  Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return  username;
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
