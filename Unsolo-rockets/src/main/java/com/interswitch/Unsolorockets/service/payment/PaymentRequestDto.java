package com.interswitch.Unsolorockets.service.payment;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRequestDto  extends PaymentInitiationRequestDto {

    private String redirectUrl;


    private BigDecimal amount;

    private Customer customer;
}
