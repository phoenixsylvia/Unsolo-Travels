package com.interswitch.Unsolorockets.service;

import com.interswitch.Unsolorockets.dtos.requests.HotelBookingDto;
import com.interswitch.Unsolorockets.exceptions.UserException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.models.HotelBooking;

public interface HotelService {
    HotelBooking bookHotel(Long userId, HotelBookingDto hotelBookingDto) throws UserException;

}
