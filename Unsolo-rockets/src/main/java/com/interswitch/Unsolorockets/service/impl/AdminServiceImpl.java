package com.interswitch.Unsolorockets.service.impl;

import com.interswitch.Unsolorockets.dtos.requests.PackageDto;
import com.interswitch.Unsolorockets.exceptions.PackageException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.models.Destination;
import com.interswitch.Unsolorockets.models.Package;
import com.interswitch.Unsolorockets.models.Traveller;
import com.interswitch.Unsolorockets.models.User;
import com.interswitch.Unsolorockets.respository.DestinationRepository;
import com.interswitch.Unsolorockets.respository.PackageRepository;
import com.interswitch.Unsolorockets.respository.TravellerRepository;
import com.interswitch.Unsolorockets.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final DestinationRepository destinationRepository;
    private final PackageRepository packageRepository;
    private final TravellerRepository travellerRepository;
//    private final UserServiceImpl userService;
    @Override
    public String createPackage(PackageDto packageDto) throws PackageException {
        if(packageDto.getTitle() == null || packageDto.getTitle().equals("")){
            throw new PackageException("Title cannot be null nor empty");
        }
        Optional< Package> packageOptional = packageRepository.findByTitle(packageDto.getTitle());
        if (packageOptional.isPresent()){
            throw new PackageException("Package with this title already exist");
        }
        Destination destination = createDestination(packageDto.getCountry(), packageDto.getState(), packageDto.getCity());
        destinationRepository.save(destination);
        Package aPackage = new Package();
        aPackage.setDestination(destination);
        aPackage.setPrice(packageDto.getPrice());
        aPackage.setTitle(packageDto.getTitle());
        packageRepository.save(aPackage);
        return "package created";
    }

    @Override
    public String deactivateUserAccount(String email) throws UserNotFoundException {
        Optional <User> optionalTraveller = travellerRepository.findByEmail(email);
        if(optionalTraveller.isEmpty()){
            throw new UserNotFoundException();
        }
        Traveller traveller = (Traveller) optionalTraveller.get();
        travellerRepository.delete(traveller);
        return "Account with email: "+traveller.getEmail()+ " has been deactivated";
    }

    private Destination createDestination(String country, String state, String city) {
        Destination destination = new Destination();
        destination.setCity(city);
        destination.setCountry(country);
        destination.setState(state);
        return destination;
    }
}

