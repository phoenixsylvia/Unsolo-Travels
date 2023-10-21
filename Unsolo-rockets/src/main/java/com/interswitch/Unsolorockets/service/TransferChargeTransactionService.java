package com.interswitch.Unsolorockets.service;

import com.interswitch.Unsolorockets.dtos.PageDto;
import com.interswitch.Unsolorockets.dtos.TransferChargeTransactionDto;
import com.interswitch.Unsolorockets.exceptions.CommonsException;

import java.math.BigDecimal;

public interface TransferChargeTransactionService {

    void recordTransferTransaction(long senderId, long receiverId, BigDecimal amount);

    PageDto getTransactionList(int page, int size);

    TransferChargeTransactionDto getTransaction(Long transactionId) throws CommonsException;
}
