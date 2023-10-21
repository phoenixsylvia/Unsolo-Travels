package com.interswitch.Unsolorockets.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidEmailException extends UserException{
    public InvalidEmailException(String message) {
        super(message, HttpStatus.NOT_ACCEPTABLE);
    }
}
