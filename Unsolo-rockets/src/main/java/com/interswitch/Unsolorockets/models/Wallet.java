package com.interswitch.Unsolorockets.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "wallet_tbl")
public class Wallet extends Base{

    @Column(nullable = false, unique = true)
    private String walletId;
    private BigDecimal balance = BigDecimal.ZERO;
    @Column(nullable = false, unique = true)
    private long userId;

    private String pin;

}
