package com.interswitch.Unsolorockets.service.payment;

import com.interswitch.Unsolorockets.exceptions.CommonsException;
import com.interswitch.Unsolorockets.models.enums.PaymentStatus;
import com.interswitch.Unsolorockets.respository.TransactionRepository;
import com.interswitch.Unsolorockets.service.payment.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentScheduler {

    private final TransactionRepository transactionRepository;

    private final PaymentService paymentService;

//    @Scheduled(cron = "0 * * * * *")
//    public void getPaymentStatus() {
//        log.info("payment status scheduler initiated");
//        List<Transaction> transactions = transactionRepository.findByStatus(PaymentStatus.PENDING.name());
//        transactions.forEach(transaction -> {
//            try {
//                PaymentLogDto paymentLogDto = paymentService.getPayment(transaction.getReference());
//                log.info("Received response from flutterwave -> [{}]", paymentLogDto);
//            } catch (CommonsException e) {
//                log.error("An error occurred -> [{}]", e.getMessage());
//            }
//        });
//    }
}
