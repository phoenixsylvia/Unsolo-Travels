package com.interswitch.Unsolorockets.service.impl;

import com.interswitch.Unsolorockets.dtos.PageDto;
import com.interswitch.Unsolorockets.dtos.TransferChargeTransactionDto;
import com.interswitch.Unsolorockets.exceptions.CommonsException;
import com.interswitch.Unsolorockets.models.TransferChargeTransaction;
import com.interswitch.Unsolorockets.respository.TransferChargeTransactionRepository;
import com.interswitch.Unsolorockets.service.TransferChargeTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferChargeTransactionServiceImpl implements TransferChargeTransactionService {

    private final TransferChargeTransactionRepository transferChargeTransactionRepository;


    @Override
    public void recordTransferTransaction(long senderId, long receiverId, BigDecimal amount) {
        TransferChargeTransaction transferChargeTransaction = new TransferChargeTransaction();
        transferChargeTransaction.setSenderId(senderId);
        transferChargeTransaction.setReceiverId(receiverId);
        transferChargeTransaction.setCharge(amount);
        transferChargeTransaction = transferChargeTransactionRepository.save(transferChargeTransaction);
        log.info("transfer charge transaction recorded [{}]", transferChargeTransaction);
    }

    @Override
    public PageDto getTransactionList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<TransferChargeTransaction> transferChargeTransactions = transferChargeTransactionRepository.findAll(pageRequest);
        List<TransferChargeTransactionDto> dtos = transferChargeTransactions.map(transferChargeTransaction -> {
            TransferChargeTransactionDto transferChargeTransactionDto = new TransferChargeTransactionDto();
            BeanUtils.copyProperties(transferChargeTransaction, transferChargeTransactionDto);
            return transferChargeTransactionDto;
        }).stream().toList();
        return PageDto.build(transferChargeTransactions, dtos);
    }

    @Override
    public TransferChargeTransactionDto getTransaction(Long transactionId) throws CommonsException {
        TransferChargeTransaction transferChargeTransaction = transferChargeTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new CommonsException("transfer record not found", HttpStatus.NOT_FOUND));
        TransferChargeTransactionDto transferChargeTransactionDto = new TransferChargeTransactionDto();
        BeanUtils.copyProperties(transferChargeTransaction, transferChargeTransactionDto);
        return transferChargeTransactionDto;
    }
}
