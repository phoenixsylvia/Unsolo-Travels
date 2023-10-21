package com.interswitch.Unsolorockets.controllers;

import com.interswitch.Unsolorockets.dtos.requests.HotelBookingDto;
import com.interswitch.Unsolorockets.exceptions.UserException;
import com.interswitch.Unsolorockets.models.HotelBooking;
import com.interswitch.Unsolorockets.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotel")
public class HotelBookingController {

    private final HotelService hotelService;

    public HotelBookingController(HotelService hotelService) {
        this.hotelService = hotelService;
    }


    @PostMapping("/")
    public ResponseEntity<?> bookHotel(
            @RequestParam Long userId, @RequestBody HotelBookingDto bookingDTO) throws UserException {
        HotelBooking booking = hotelService.bookHotel(userId, bookingDTO);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }
}
