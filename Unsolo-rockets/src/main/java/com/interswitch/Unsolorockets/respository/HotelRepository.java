package com.interswitch.Unsolorockets.respository;

import com.interswitch.Unsolorockets.models.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<HotelBooking, Long> {
}
