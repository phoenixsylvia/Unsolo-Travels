package com.interswitch.Unsolorockets.dtos.responses;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
//@RequiredArgsConstructor
public class TripResponse {

    private String travellerName;
    private String country;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private String aboutTheTrip;
    private String journeyType;
    private boolean splitCost;
    private double budget;
    private boolean firstTime;

}
