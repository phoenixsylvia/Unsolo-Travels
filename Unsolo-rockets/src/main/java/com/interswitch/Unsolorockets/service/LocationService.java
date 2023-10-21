package com.interswitch.Unsolorockets.service;

import com.interswitch.Unsolorockets.dtos.responses.LiveLocationDto;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;


public interface LocationService {
    String extractTravellerCoordinates();
    void emergencyAlert() throws UserNotFoundException;
    LiveLocationDto extractCityAndState();
}
