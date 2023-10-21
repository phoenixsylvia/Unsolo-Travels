package com.interswitch.Unsolorockets.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@Entity(name = "transfer_charge_transactions")
@ToString
public class TransferChargeTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private BigDecimal charge;

    @Column(nullable = false, updatable = false)
    private long senderId;

    @Column(nullable = false, updatable = false)
    private long receiverId;

    @CreationTimestamp
    private Date createdAt;
}