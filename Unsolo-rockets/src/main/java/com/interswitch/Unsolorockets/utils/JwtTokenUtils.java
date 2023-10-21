package com.interswitch.Unsolorockets.utils;

import com.interswitch.Unsolorockets.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
public class JwtTokenUtils {

        private static final String JWT_SECRET = "your-secret-key";// TODO: 04/09/2023 we should hide the key , choose a secret key and hide it
        private static final long JWT_EXPIRATION_MS = 3600000;

        public static String generateToken(User user) {
            Claims claims = Jwts.claims().setSubject(user.getId().toString());
            claims.put("email", user.getEmail());
            claims.put("roles", user.getRole());

            Date now = new Date();
            Date expiration = new Date(now.getTime() + JWT_EXPIRATION_MS);

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                    .compact();
        }

        public static String generateEmailVerificationToken(String email ) {
            Map<String, Object> claims = new HashMap<>();
            return createToken(claims, email);
        }

    private static String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
    }

    }
