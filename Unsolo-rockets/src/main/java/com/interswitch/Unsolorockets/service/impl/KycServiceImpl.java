package com.interswitch.Unsolorockets.service.impl;

import com.interswitch.Unsolorockets.dtos.responses.KycResponse;
import com.interswitch.Unsolorockets.exceptions.InvalidNinValidationException;
import com.interswitch.Unsolorockets.exceptions.KycVerifiedException;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.models.Traveller;
import com.interswitch.Unsolorockets.models.User;
import com.interswitch.Unsolorockets.respository.TravellerRepository;
import com.interswitch.Unsolorockets.security.CustomUserDetailService;
import com.interswitch.Unsolorockets.security.IPasswordEncoder;
import com.interswitch.Unsolorockets.security.JwtUtils;
import com.interswitch.Unsolorockets.service.KycService;
import com.interswitch.Unsolorockets.utils.AppUtils;
import com.interswitch.Unsolorockets.utils.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.interswitch.Unsolorockets.utils.UserUtil.getLoggedInUser;

@Service
public class KycServiceImpl implements KycService {
    private final WebClient webClient;
    @Autowired
    private final TravellerRepository travellerRepository;
    @Autowired
    private  AppUtils appUtils;
    private final IPasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private String beeceptorUrl = "https://kyc-validation.free.beeceptor.com";

    public KycServiceImpl(WebClient.Builder builder, TravellerRepository travellerRepository,
                          AppUtils appUtils, IPasswordEncoder encoder, CustomUserDetailService customUserDetailService, JwtUtils jwtUtils) {
        this.webClient = builder
                .baseUrl(beeceptorUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
        this.travellerRepository = travellerRepository;
        this.appUtils = appUtils;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    public Mono<?> ninValidationRequest(String ninId) throws InvalidNinValidationException, UserNotFoundException {
        CustomUser loggedInUser = getLoggedInUser();

        User user = travellerRepository.findByEmail(loggedInUser.getEmail()).orElseThrow(() ->new UsernameNotFoundException("User not found"));

        if (!AppUtils.validateNinId(ninId)) {
            throw new IllegalArgumentException("NIN must be 11 digits");
        }
        if (user.getNinId() != null) {
            return Mono.just("Verified");
        }

        Mono<?> responseJson = webClient
                    .get()
                    .uri("/nin/{nin_id}", ninId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(
                            // Check for specific HTTP status codes and handle them accordingly
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> {
                                //Handles Errors
                                if (clientResponse.statusCode().is4xxClientError()) {
                                    return Mono.error(new RuntimeException("Bad Request"));
                                } else {
                                    return Mono.error(new RuntimeException("An error occurred"));
                                }
                            })
                    .bodyToMono(KycResponse.class) // Deserialize the response into KYCResponse
                    .flatMap(kycResponse -> {

                        // Create a new Traveller object and set appropriate fields
                        Traveller authUser = (Traveller) user;
                        authUser.setNinId(encoder.encode(ninId));
                        authUser.setKycVerified(true);
                       travellerRepository.save(authUser);

                        return Mono.just("NIN Verification Successful");
                    })
                    .switchIfEmpty(Mono.empty());
              return responseJson;


    }

}

