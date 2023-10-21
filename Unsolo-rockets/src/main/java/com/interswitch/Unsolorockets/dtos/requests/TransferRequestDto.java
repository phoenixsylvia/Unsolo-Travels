package com.interswitch.Unsolorockets.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequestDto {
    private String walletId;
    private BigDecimal amount;
    private String pin;


}
