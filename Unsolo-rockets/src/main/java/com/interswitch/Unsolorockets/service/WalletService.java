package com.interswitch.Unsolorockets.service;

import com.interswitch.Unsolorockets.dtos.responses.TransferResponse;
import com.interswitch.Unsolorockets.dtos.requests.CreateWalletRequest;
import com.interswitch.Unsolorockets.dtos.requests.TransferRequestDto;
import com.interswitch.Unsolorockets.dtos.responses.WalletDto;
import com.interswitch.Unsolorockets.exceptions.CommonsException;

import java.io.IOException;

public interface WalletService {

    void createWallet(CreateWalletRequest createWalletRequest) throws Exception;

    WalletDto getWallet() throws CommonsException;

    TransferResponse transfer(TransferRequestDto transferRequestDto) throws CommonsException, IOException;
}
