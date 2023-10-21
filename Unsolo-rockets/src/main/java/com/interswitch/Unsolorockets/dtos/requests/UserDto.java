package com.interswitch.Unsolorockets.dtos.requests;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private String firstName;
    private String lastName;
    private String password;
    private String date;
    private String email;
    private String phoneNumber;
    private String gender;
    private String role;
}
