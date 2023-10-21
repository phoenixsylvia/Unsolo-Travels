package com.interswitch.Unsolorockets.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdateRequest {

    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String gender;
    private String location;
    private String description;
    private String profilePicture;

}
