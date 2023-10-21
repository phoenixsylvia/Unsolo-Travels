package com.interswitch.Unsolorockets.controllers;

import com.interswitch.Unsolorockets.exceptions.InvalidNinValidationException;
import com.interswitch.Unsolorockets.exceptions.UserAlreadyExistException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.service.KycService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
public class KycController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KycController.class);
    private final KycService kycService;

    @GetMapping("/nin/{nin_id}")
    public ResponseEntity<Mono<?>> verifyNIN(@PathVariable(value = "nin_id") String ninId) throws InvalidNinValidationException, IllegalArgumentException, UserAlreadyExistException, UserNotFoundException {
        var kyc = kycService.ninValidationRequest(ninId);
        LOGGER.info("Verify NIN: {}", ninId);
        return new ResponseEntity<>(kyc, HttpStatus.OK);
}
}
