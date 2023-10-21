package com.interswitch.Unsolorockets.models;

import com.interswitch.Unsolorockets.models.enums.JourneyType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "trips")
@ToString
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private long travellerId;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private LocalDate departureDate;
    @Column(nullable = false)
    private LocalDate arrivalDate;
    @Column(nullable = false)
    private String aboutTheTrip;
    @Column(nullable = false)
    private JourneyType journeyType;
    @Column(nullable = false)
    private boolean splitCost;
    @Column(nullable = false)
    private double budget;
    @Column(nullable = false)
    private boolean firstTime;
}
