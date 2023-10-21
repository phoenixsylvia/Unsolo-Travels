package com.interswitch.Unsolorockets.exceptions;

import org.springframework.http.HttpStatus;

public class JwtException extends CommonsException {
    public JwtException(String message, HttpStatus status) {
        super(message, status);
    }
}
