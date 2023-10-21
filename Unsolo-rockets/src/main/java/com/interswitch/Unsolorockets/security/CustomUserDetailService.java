package com.interswitch.Unsolorockets.security;

import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.models.User;
import com.interswitch.Unsolorockets.respository.AdminRepository;
import com.interswitch.Unsolorockets.respository.TravellerRepository;
import com.interswitch.Unsolorockets.utils.CustomUser;
import lombok.AllArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
@JsonComponent
public class CustomUserDetailService implements UserDetailsService {

    private final TravellerRepository travellerRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user;
        try {

            Optional <User> userOptional = adminRepository.findByEmail(email);
            if(userOptional.isEmpty()){
                userOptional = travellerRepository.findByEmail(email);
            }
            if(userOptional.isEmpty()){
                throw new UserNotFoundException();
            }
            user = userOptional.get();

        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new CustomUser(user.getId(), user.getPassword(),user.getEmail(), user.getRole());
    }
}
