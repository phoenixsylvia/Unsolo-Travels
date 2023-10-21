package com.interswitch.Unsolorockets.respository;

import com.interswitch.Unsolorockets.models.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Page<Trip> findTripsByTravellerId(PageRequest pageRequest, long travellerId);
//    Page<Trip> findAll(PageRequest pageRequest);
}

