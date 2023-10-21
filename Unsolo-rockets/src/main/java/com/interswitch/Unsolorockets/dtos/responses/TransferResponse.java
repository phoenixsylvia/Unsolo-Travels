package com.interswitch.Unsolorockets.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class TransferResponse {

    private String message;

    private BigDecimal amount;

    private String walletId;
}
