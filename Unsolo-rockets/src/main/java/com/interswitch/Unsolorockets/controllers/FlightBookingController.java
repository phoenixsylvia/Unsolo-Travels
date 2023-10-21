package com.interswitch.Unsolorockets.controllers;

import com.interswitch.Unsolorockets.dtos.requests.FlightBookingDto;
import com.interswitch.Unsolorockets.exceptions.UserException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.models.FlightBooking;
import com.interswitch.Unsolorockets.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flight")
public class FlightBookingController {

    private final FlightService flightService;

    public FlightBookingController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/")
    public ResponseEntity<?> bookFlight(@RequestParam Long userId, @RequestBody FlightBookingDto bookingDTO) {
        try {
            FlightBooking booking = flightService.bookFlight(userId, bookingDTO);
            return ResponseEntity.ok(booking);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserException e) {
            throw new RuntimeException(e);
        }
    }
}