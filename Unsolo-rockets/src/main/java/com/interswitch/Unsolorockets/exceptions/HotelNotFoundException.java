package com.interswitch.Unsolorockets.exceptions;

import org.springframework.http.HttpStatus;

public class HotelNotFoundException extends CommonsException{

    public HotelNotFoundException(String message, HttpStatus status) {
        super(message,status);
    }
}
