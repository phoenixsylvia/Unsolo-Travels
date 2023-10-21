package com.interswitch.Unsolorockets.respository;

import com.interswitch.Unsolorockets.models.Traveller;
import com.interswitch.Unsolorockets.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravellerRepository extends JpaRepository<Traveller, Long> {
    Optional<User> findByEmail(String email);
}
