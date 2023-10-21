package com.interswitch.Unsolorockets.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class TransferChargeDto {

    private BigDecimal charge;
}
