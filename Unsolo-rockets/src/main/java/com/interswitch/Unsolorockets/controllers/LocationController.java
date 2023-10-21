package com.interswitch.Unsolorockets.controllers;


import com.interswitch.Unsolorockets.dtos.responses.LiveLocationDto;
import com.interswitch.Unsolorockets.service.impl.LocationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationServiceImpl locationService;

    @GetMapping("/coordinates")
    public ResponseEntity<String> getTravelerCoordinates() {
        String coordinates = locationService.extractTravellerCoordinates();

        if (coordinates != null) {
            return new ResponseEntity<>(coordinates, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to retrieve coordinates.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/city-state")
    public ResponseEntity<?> getCityAndState() {
        try {
            LiveLocationDto liveLocation = locationService.extractCityAndState();
            if (liveLocation.getCity() != null && liveLocation.getState() != null) {
                return new ResponseEntity<>(liveLocation, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to retrieve coordinates.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/emergency-alert")
    public ResponseEntity<String> sendEmergencyAlert() {
        try {
            locationService.emergencyAlert();
            return ResponseEntity.ok("Emergency alert sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the emergency alert.");
        }
    }
}
