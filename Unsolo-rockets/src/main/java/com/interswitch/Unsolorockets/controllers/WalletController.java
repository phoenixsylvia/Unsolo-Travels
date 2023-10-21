package com.interswitch.Unsolorockets.controllers;

import com.interswitch.Unsolorockets.dtos.responses.TransferResponse;
import com.interswitch.Unsolorockets.dtos.requests.CreateWalletRequest;
import com.interswitch.Unsolorockets.dtos.requests.TransferRequestDto;
import com.interswitch.Unsolorockets.exceptions.CommonsException;
import com.interswitch.Unsolorockets.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createWallet(@RequestBody CreateWalletRequest createWalletRequest) throws Exception {
        walletService.createWallet(createWalletRequest);
    }

    @GetMapping("wallet-info")
    public ResponseEntity<?> getUserWallet() throws CommonsException {
        return new ResponseEntity<>(walletService.getWallet(), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequestDto transferRequestDto) throws CommonsException, IOException {
        TransferResponse response = walletService.transfer(transferRequestDto);
        return ResponseEntity.ok(response);
    }
}