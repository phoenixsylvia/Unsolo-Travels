package com.interswitch.Unsolorockets.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class TransferChargeTransactionDto {

    private long id;

    private BigDecimal charge;

    private long senderId;

    private long receiverId;

    private Date createdAt;
}
