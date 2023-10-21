package com.interswitch.Unsolorockets.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
public class HotelBookingDto {
    private String hotel;

    private String roomType;

    private int guestNumber;

    private String checkInDate;

    private String checkOutDate;
}
