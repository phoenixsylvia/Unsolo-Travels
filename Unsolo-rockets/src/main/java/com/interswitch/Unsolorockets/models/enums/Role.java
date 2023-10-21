package com.interswitch.Unsolorockets.models.enums;

public enum Role {
    ADMIN, TRAVELLER;

    public static final String ADMIN_PREAUTHORIZE = "hasAuthority('ADMIN')";

    public static final String USER_PREAUTHORIZE = "hasAuthority('USER')";

}
