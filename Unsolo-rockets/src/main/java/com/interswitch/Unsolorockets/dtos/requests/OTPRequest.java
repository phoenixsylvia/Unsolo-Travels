package com.interswitch.Unsolorockets.dtos.requests;


import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@RequiredArgsConstructor
public class OTPRequest {

    private String otp;
    private String emailForOTP;
}
