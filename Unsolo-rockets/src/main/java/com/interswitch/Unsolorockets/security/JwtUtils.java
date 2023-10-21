package com.interswitch.Unsolorockets.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {
    @Value("${Unsolo-Travels}")
    private String JWT_SECRET;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean hasClaim(String token, String claimName) {
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName) != null;
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            throw new JwtException(e.getMessage());
        }
        return claims;
    }

    public Boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    public String generateToken(Authentication authentication) {

        String auth = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        return createToken(auth, authentication);
    }

    private String createToken(String claims, Authentication authentication) {
        log.info(String.valueOf(authentication.getName()));
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET).compact();
    }

    public String generateRegistrationConfirmationToken(String email) {
        Date expirationDate = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24));
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String resetPasswordToken(String email) {
        Date currentDate = new Date();
        Date expirationDate = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }
}