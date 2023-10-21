package com.interswitch.Unsolorockets.service;

import com.interswitch.Unsolorockets.dtos.requests.FlightBookingDto;
import com.interswitch.Unsolorockets.exceptions.UserException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.models.FlightBooking;

public interface FlightService {
    FlightBooking bookFlight(Long userId, FlightBookingDto bookingDTO) throws UserException;
}
