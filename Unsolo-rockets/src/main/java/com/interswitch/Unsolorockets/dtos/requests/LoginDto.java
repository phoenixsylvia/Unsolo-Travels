package com.interswitch.Unsolorockets.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {

    private String email;

    private String password;
}
