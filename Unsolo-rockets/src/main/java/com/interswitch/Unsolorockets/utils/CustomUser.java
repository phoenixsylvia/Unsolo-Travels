package com.interswitch.Unsolorockets.utils;

import com.interswitch.Unsolorockets.models.enums.Gender;
import com.interswitch.Unsolorockets.models.enums.Role;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Setter
@Getter
public class CustomUser implements UserDetails {
    private Long id;

    private String firstName;

    private String lastName;

    public CustomUser() {
    }

    public CustomUser(Long id, String password, String email, Role role) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    private String password;


    private String phoneNumber;

    private String email;

    @Column(nullable = false)
    private Gender gender;

    private boolean isVerified;
    private LocalDate dateOfBirth;

    private Role role;
    private String validOTP;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
