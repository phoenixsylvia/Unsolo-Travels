package com.interswitch.Unsolorockets.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "travellers")
public class Traveller extends User {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FlightBooking> flightBookings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<HotelBooking> hotelBookings = new ArrayList<>();


}
