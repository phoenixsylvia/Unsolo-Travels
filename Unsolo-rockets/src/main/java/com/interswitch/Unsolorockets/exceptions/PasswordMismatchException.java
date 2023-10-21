package com.interswitch.Unsolorockets.exceptions;

import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends UserException {

    public PasswordMismatchException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
