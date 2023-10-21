package com.interswitch.Unsolorockets.service.impl;

import com.interswitch.Unsolorockets.dtos.requests.HotelBookingDto;
import com.interswitch.Unsolorockets.exceptions.UserException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.models.HotelBooking;
import com.interswitch.Unsolorockets.models.Traveller;
import com.interswitch.Unsolorockets.respository.HotelRepository;
import com.interswitch.Unsolorockets.respository.TravellerRepository;
import com.interswitch.Unsolorockets.service.HotelService;
import com.interswitch.Unsolorockets.utils.AppUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService {


    private final HotelRepository hotelRepository;

    private final TravellerRepository userRepository;

    private final AppUtils appUtils;



    @Override
    public HotelBooking bookHotel(Long userId, HotelBookingDto hotelBookingDto) throws UserException {
        Traveller user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        LocalDate departureDate = appUtils.createLocalDate(hotelBookingDto.getCheckInDate());
        LocalDate arrivalDate = appUtils.createLocalDate(hotelBookingDto.getCheckOutDate());
        HotelBooking booking = new HotelBooking();
        booking.setUser(user);
        booking.setHotel(hotelBookingDto.getHotel());
        booking.setGuestNumber(hotelBookingDto.getGuestNumber());
        booking.setCheckInDate(arrivalDate);
        booking.setCheckOutDate(departureDate);
        booking.setRoomType(hotelBookingDto.getRoomType());

        hotelRepository.save(booking);

        return booking;
    }
}

