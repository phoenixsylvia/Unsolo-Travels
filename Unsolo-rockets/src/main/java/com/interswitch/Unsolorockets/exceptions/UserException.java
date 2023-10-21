package com.interswitch.Unsolorockets.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends CommonsException {

    public UserException(String message, HttpStatus status) {
        super(message, status);
    }

}
