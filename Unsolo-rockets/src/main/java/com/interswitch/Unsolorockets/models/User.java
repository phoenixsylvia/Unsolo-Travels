package com.interswitch.Unsolorockets.models;

import com.interswitch.Unsolorockets.models.enums.Gender;
import com.interswitch.Unsolorockets.models.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Date;


@MappedSuperclass
@Setter
@Getter
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String firstName;

    @Column(nullable = false)
    @NotBlank
    private String lastName;

    @Column(nullable = false)
    @NotBlank
    private String password;


    private String phoneNumber;

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private Gender gender;

    private boolean isVerified;
    @Column
    private LocalDate dateOfBirth;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
    private String validOTP;

    private String tokenForEmail;
    @Column(unique = true)
    private String ninId;
    private Boolean kycVerified;
    @CreationTimestamp
    private Date createdAt;

    @Column
    private String location;

    @Column
    private String description;

    @Column
    private String profilePicture;
}
