package com.interswitch.Unsolorockets.service.impl;

import com.interswitch.Unsolorockets.dtos.requests.FlightBookingDto;
import com.interswitch.Unsolorockets.exceptions.UserException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.models.FlightBooking;
import com.interswitch.Unsolorockets.models.Traveller;
import com.interswitch.Unsolorockets.respository.FlightRepository;
import com.interswitch.Unsolorockets.respository.TravellerRepository;
import com.interswitch.Unsolorockets.service.FlightService;
import com.interswitch.Unsolorockets.utils.AppUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    private final TravellerRepository userRepository;

    private final AppUtils appUtils;

    @Override
    public FlightBooking bookFlight(Long userId, FlightBookingDto bookingDTO) throws UserException {
        Traveller user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        LocalDate departureDate = appUtils.createLocalDate(bookingDTO.getDepartureDate());
        LocalDate arrivalDate = appUtils.createLocalDate(bookingDTO.getReturnDate());
        FlightBooking booking = new FlightBooking();
        booking.setUser(user);
        booking.setDepartureLocation(bookingDTO.getDepartureLocation());
        booking.setArrivalLocation(bookingDTO.getArrivalLocation());
        booking.setDepartureDate(departureDate);
        booking.setArrivalDate(arrivalDate);

        flightRepository.save(booking);

        return booking;
    }

}





