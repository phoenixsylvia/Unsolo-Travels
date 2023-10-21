package com.interswitch.Unsolorockets.service.payment;

import com.interswitch.Unsolorockets.models.enums.PaymentCurrency;
import com.interswitch.Unsolorockets.models.enums.PaymentStatus;
import com.interswitch.Unsolorockets.models.enums.Processor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PaymentInitiationResponse {
    private String paymentLink;
    private BigDecimal amount;
    private String paymentReference;
    private PaymentCurrency currency;
    private PaymentStatus status;
    private String processorReference;
    private Processor processor;
}
