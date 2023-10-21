package com.interswitch.Unsolorockets.dtos.requests;

import com.interswitch.Unsolorockets.models.Destination;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PackageDto {
    private String title;
    private double price;
    private String country;
    private String state;
    private String city;

}
