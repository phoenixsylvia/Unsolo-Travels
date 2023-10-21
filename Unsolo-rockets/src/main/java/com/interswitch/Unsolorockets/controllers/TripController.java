package com.interswitch.Unsolorockets.controllers;

import com.interswitch.Unsolorockets.dtos.requests.DeleteRequest;
import com.interswitch.Unsolorockets.dtos.requests.TripRequest;
import com.interswitch.Unsolorockets.dtos.responses.BuddyResponse;
import com.interswitch.Unsolorockets.dtos.responses.TripResponse;
import com.interswitch.Unsolorockets.exceptions.TripNotFoundException;
import com.interswitch.Unsolorockets.exceptions.UserException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/trip")
@Slf4j
public class TripController {
    private final TripService tripService;

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody TripRequest request) throws UserException {
        var response = tripService.createTrip(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/")
    public ResponseEntity<?> updateTrip(@RequestBody TripRequest request) throws UserException, TripNotFoundException {
        var response = tripService.updateTripDetails(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity <?> deleteTrip(@RequestBody DeleteRequest request) throws UserNotFoundException, TripNotFoundException {
        var response = tripService.deleteTrip(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/match")
    public ResponseEntity<List<BuddyResponse>> findMatchingTravellers(@RequestBody TripRequest filterRequest) {
        List<BuddyResponse> matchingTravellers = tripService.findMatchingTravellers(filterRequest);
        return new ResponseEntity<>(matchingTravellers, HttpStatus.OK);
    }

    @GetMapping("{travellerId}")
    public ResponseEntity<?> viewTravellerTrips(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @PathVariable long travellerId){
        PageRequest pageRequest = PageRequest.of(page, size);
        var response = tripService.findTravellerTrips(pageRequest, travellerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> viewAllTrips(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<TripResponse> tripResponsePage = tripService.findAllTrips(pageRequest);
        return new ResponseEntity<>(tripResponsePage, HttpStatus.OK);
    }
}
