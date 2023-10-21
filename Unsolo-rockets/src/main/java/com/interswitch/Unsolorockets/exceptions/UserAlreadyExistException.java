package com.interswitch.Unsolorockets.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends UserException {

    public UserAlreadyExistException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
