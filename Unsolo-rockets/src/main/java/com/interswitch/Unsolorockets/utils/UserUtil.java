package com.interswitch.Unsolorockets.utils;

import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

    public static CustomUser getLoggedInUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
