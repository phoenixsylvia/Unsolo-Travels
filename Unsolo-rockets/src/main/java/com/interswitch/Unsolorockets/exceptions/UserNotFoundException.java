package com.interswitch.Unsolorockets.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
        super("user.not_found", HttpStatus.NOT_FOUND);
    }
}
