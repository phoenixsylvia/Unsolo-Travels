package com.interswitch.Unsolorockets.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
//@RequiredArgsConstructor
public class BuddyResponse {

    private Long buddyId;
    private String buddyName;
    private String country;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private String aboutTheTrip;
    private String journeyType;
    private boolean splitCost;
    private double budget;
    private boolean firstTime;

}
