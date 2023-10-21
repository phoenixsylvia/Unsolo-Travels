package com.interswitch.Unsolorockets.controllers;


import com.interswitch.Unsolorockets.dtos.requests.LoginDto;
import com.interswitch.Unsolorockets.dtos.requests.OTPRequest;
import com.interswitch.Unsolorockets.dtos.requests.UserDto;
import com.interswitch.Unsolorockets.dtos.requests.UserUpdateRequest;
import com.interswitch.Unsolorockets.dtos.responses.DashboardResponse;
import com.interswitch.Unsolorockets.dtos.responses.LoginResponse;
import com.interswitch.Unsolorockets.dtos.responses.UserProfileResponse;
import com.interswitch.Unsolorockets.exceptions.InvalidCredentialsException;
import com.interswitch.Unsolorockets.exceptions.UserException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.security.JwtUtils;
import com.interswitch.Unsolorockets.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> signUp(@RequestBody UserDto userDto) throws UserException, IOException {
        UserProfileResponse response = userService.createUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginRequest) throws InvalidCredentialsException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        LoginResponse response = new LoginResponse(jwtUtils.generateToken(authentication));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyToken(@RequestBody OTPRequest otpRequest) throws UserNotFoundException {
        return new ResponseEntity<>(userService.verifyOTP(otpRequest), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UserProfileResponse> updateUserByEmail(@RequestParam String email, @RequestBody UserUpdateRequest userUpdateRequest) throws UserNotFoundException {
        UserProfileResponse response = userService.updateUserDetails(email, userUpdateRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<DashboardResponse> getUserProfile(@RequestParam String email) throws UserNotFoundException {
        DashboardResponse userProfile = userService. userDashboard(email);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }
}
