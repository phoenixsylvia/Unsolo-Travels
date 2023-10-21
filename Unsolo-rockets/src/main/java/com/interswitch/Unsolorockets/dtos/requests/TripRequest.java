package com.interswitch.Unsolorockets.dtos.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TripRequest {

    private String travellerId;
    private String country;
    private String departureDate;
    private String arrivalDate;
    private String aboutTheTrip;
    private String journeyType;
    private boolean splitCost;
    private double budget;
    private boolean firstTime;
    private Long tripId;
}
