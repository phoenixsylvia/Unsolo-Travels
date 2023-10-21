package com.interswitch.Unsolorockets.respository;

import com.interswitch.Unsolorockets.models.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<FlightBooking, Long> {
}