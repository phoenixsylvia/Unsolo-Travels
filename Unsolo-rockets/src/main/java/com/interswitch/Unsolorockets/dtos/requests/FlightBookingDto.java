package com.interswitch.Unsolorockets.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class FlightBookingDto {

    private String departureLocation;

    private String arrivalLocation;

    private String departureDate;
    private String ReturnDate;
}
