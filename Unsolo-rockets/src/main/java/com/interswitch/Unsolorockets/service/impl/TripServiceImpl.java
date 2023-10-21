package com.interswitch.Unsolorockets.service.impl;

import com.interswitch.Unsolorockets.dtos.requests.DeleteRequest;
import com.interswitch.Unsolorockets.dtos.requests.TripRequest;
import com.interswitch.Unsolorockets.dtos.responses.BuddyResponse;
import com.interswitch.Unsolorockets.dtos.responses.TripResponse;
import com.interswitch.Unsolorockets.exceptions.TripNotFoundException;
import com.interswitch.Unsolorockets.exceptions.UserException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.models.Traveller;
import com.interswitch.Unsolorockets.models.Trip;
import com.interswitch.Unsolorockets.models.enums.JourneyType;
import com.interswitch.Unsolorockets.respository.TravellerRepository;
import com.interswitch.Unsolorockets.respository.TripRepository;
import com.interswitch.Unsolorockets.service.TripService;
import com.interswitch.Unsolorockets.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;
    private final TravellerRepository travellerRepository;
    private final AppUtils appUtils;


    @Override
    public TripResponse createTrip(TripRequest request) throws UserException {
        log.info(String.valueOf(request));

        Optional<Traveller> optionalTraveller = travellerRepository.findById(Long.valueOf(request.getTravellerId()));
        log.info(String.valueOf(request));
        if (optionalTraveller.isEmpty()) {
            throw new UserNotFoundException();
        }

        log.info(String.valueOf(request));

        Traveller traveller = optionalTraveller.get();
        Trip trip = new Trip();
        BeanUtils.copyProperties(request, trip);

        log.info(String.valueOf(trip));

        LocalDate departureDate = appUtils.createLocalDate(request.getDepartureDate());
        LocalDate arrivalDate = appUtils.createLocalDate(request.getArrivalDate());
        trip.setDepartureDate(departureDate);
        trip.setArrivalDate(arrivalDate);
        trip.setTravellerId(traveller.getId());
        trip.setJourneyType(JourneyType.valueOf(request.getJourneyType().toUpperCase()));

        log.info(String.valueOf(request));
        tripRepository.save(trip);

        TripResponse tripResponse = new TripResponse();
        BeanUtils.copyProperties(request, tripResponse);
        tripResponse.setTravellerName(traveller.getFirstName());
        return tripResponse;
    }

    @Override
    public TripResponse updateTripDetails(TripRequest request) throws UserException, TripNotFoundException {
        Optional <Traveller> userOptional = travellerRepository.findById(Long.valueOf(request.getTravellerId()));
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        Traveller traveller = userOptional.get();

        Optional<Trip> tripOptional = tripRepository.findById(request.getTripId());
        if(tripOptional.isEmpty()){
            throw new TripNotFoundException("Trip not found");
        }
        Trip trip = tripOptional.get();

        LocalDate arrivalDate = appUtils.createLocalDate(request.getArrivalDate());
        LocalDate departureDate = appUtils.createLocalDate(request.getDepartureDate());

        if (request.getAboutTheTrip() != null) {
            trip.setAboutTheTrip(request.getAboutTheTrip());
        }

        if (request.getDepartureDate() != null) {
            trip.setDepartureDate(departureDate);
        }

        if (request.getArrivalDate() != null) {
            trip.setArrivalDate(arrivalDate);
        }

        if (request.getBudget() > 0.00) {
            trip.setBudget(request.getBudget());
        }

        if (request.isFirstTime() != trip.isFirstTime()) {
            trip.setFirstTime(request.isFirstTime());
        }
        if (request.isSplitCost() != trip.isSplitCost()){
            trip.setSplitCost(request.isSplitCost());
        }
        if (request.getCountry() != null){
            trip.setCountry(request.getCountry());
        }
        if (request.getJourneyType() != null){
            trip.setJourneyType(JourneyType.valueOf(request.getJourneyType().toUpperCase()));
        }

        Trip savedTrip = tripRepository.save(trip);
        TripResponse tripResponse = createTripResponse(savedTrip);
        tripResponse.setTravellerName(traveller.getFirstName());
        tripResponse.setArrivalDate(arrivalDate);
        tripResponse.setDepartureDate(departureDate);
        return tripResponse;
    }

    @Override
    public String deleteTrip(DeleteRequest request) throws TripNotFoundException, UserNotFoundException {
        Optional <Trip> tripOptional = Optional.of(tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new TripNotFoundException("Trip not found")));
        Trip trip = tripOptional.get();
        if(trip.getTravellerId() != request.getTravellerId()){
            throw new UserNotFoundException();
        }
        tripRepository.delete(trip);
        return "Delete success";
    }
    @Override
    public List<BuddyResponse> findMatchingTravellers(TripRequest filterRequest) {
        List<Trip> trips = tripRepository.findAll();

        final LocalDate arrivalDate;
        if(filterRequest.getArrivalDate() != null) {
            arrivalDate = appUtils.createLocalDate(filterRequest.getDepartureDate());
        }else {
            arrivalDate = null;
        }
        // Step 1: Filter by country
        List<Trip> matchingTrips = trips.stream()
                .filter(trip -> trip.getCountry().equalsIgnoreCase(filterRequest.getCountry()))
                .filter(trip -> trip.getJourneyType().toString().equalsIgnoreCase(filterRequest.getJourneyType()))
                .filter(trip -> arrivalDate == null || trip.getArrivalDate().isEqual(arrivalDate))
                .toList();

//        // Step 2: Check if country matched
//        if (matchingTrips.isEmpty()) {
//            return Collections.emptyList(); // No match if the country doesn't match
//        }
//
//
//        // Step 3: Filter by journey type
//        matchingTrips = matchingTrips.stream()
//                .filter(trip -> trip.getJourneyType().toString().equalsIgnoreCase(filterRequest.getJourneyType()))
//                .collect(Collectors.toList());

        // Step 4: Map to traveler names
        List<BuddyResponse> matchingTravellers = matchingTrips.stream()
                .map(this::convertTripToTripResponse)
                .collect(Collectors.toList());

        return matchingTravellers;
    }

    private BuddyResponse convertTripToTripResponse(Trip trip) {
        return BuddyResponse.builder()
                .buddyId(trip.getTravellerId())
                .buddyName(getTravellerName(trip.getTravellerId()))
                .aboutTheTrip(trip.getAboutTheTrip())
                .country(trip.getCountry())
                .splitCost(trip.isSplitCost())
                .arrivalDate(trip.getArrivalDate())
                .departureDate(trip.getDepartureDate())
                .firstTime(trip.isFirstTime())
                .journeyType(trip.getJourneyType().toString())
                .budget(trip.getBudget())
                .build();
    }

    @Override
    public Page<TripResponse> findTravellerTrips(PageRequest pageRequest, long travellerId) {
        Page <Trip> tripPage = tripRepository.findTripsByTravellerId(pageRequest, travellerId);
            Page <TripResponse> tripResponsePage = tripPage.map(this::createTripResponse);
            return new PageImpl<>(tripResponsePage.getContent(), pageRequest, tripPage.getTotalElements());

    }

    @Override
    public Page<TripResponse> findAllTrips(PageRequest pageRequest) {
        Page<Trip> tripPage =  tripRepository.findAll(pageRequest);
        Page<TripResponse> tripResponsePage =  tripPage.map(this::createTripResponse);
        return new PageImpl<>(tripResponsePage.getContent(), pageRequest, tripPage.getTotalElements());
    }

    private String getTravellerName(Long travelerId) {
        Optional<Traveller> optionalTraveller = travellerRepository.findById(travelerId);
        if (optionalTraveller.isPresent()) {
            Traveller traveller = optionalTraveller.get();
            return traveller.getFirstName() + " " + traveller.getLastName();
        }
        return ""; // Return an empty string if the traveler is not found
    }

    private TripResponse createTripResponse(Trip trip){
        TripResponse tripResponse = new TripResponse();
        BeanUtils.copyProperties(trip, tripResponse);
        return tripResponse;
    }
}
