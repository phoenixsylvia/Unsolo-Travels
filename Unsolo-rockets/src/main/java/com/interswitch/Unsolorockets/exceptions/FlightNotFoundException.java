package com.interswitch.Unsolorockets.exceptions;

import org.springframework.http.HttpStatus;

public class FlightNotFoundException extends CommonsException {
    public FlightNotFoundException(String message, HttpStatus status) {
        super(message,status);
    }

}
