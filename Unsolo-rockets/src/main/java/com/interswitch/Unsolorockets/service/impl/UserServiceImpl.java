package com.interswitch.Unsolorockets.service.impl;

import com.interswitch.Unsolorockets.dtos.requests.OTPRequest;
import com.interswitch.Unsolorockets.dtos.requests.UserDto;
import com.interswitch.Unsolorockets.dtos.requests.UserUpdateRequest;
import com.interswitch.Unsolorockets.dtos.responses.DashboardResponse;
import com.interswitch.Unsolorockets.dtos.responses.UserProfileResponse;
import com.interswitch.Unsolorockets.exceptions.*;
import com.interswitch.Unsolorockets.models.Admin;
import com.interswitch.Unsolorockets.models.Traveller;
import com.interswitch.Unsolorockets.models.User;
import com.interswitch.Unsolorockets.models.enums.Gender;
import com.interswitch.Unsolorockets.models.enums.Role;
import com.interswitch.Unsolorockets.respository.AdminRepository;
import com.interswitch.Unsolorockets.respository.TravellerRepository;
import com.interswitch.Unsolorockets.security.IPasswordEncoder;
import com.interswitch.Unsolorockets.service.EmailService;
import com.interswitch.Unsolorockets.service.TripService;
import com.interswitch.Unsolorockets.service.UserService;
import com.interswitch.Unsolorockets.utils.AppUtils;
import com.interswitch.Unsolorockets.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final TravellerRepository travellerRepository;
    private final AdminRepository adminRepository;

    private final IPasswordEncoder passwordEncoder;

    private final EmailService emailService;
    private final HttpServletRequest request;
    private final AppUtils appUtils;

    private static void assignRole(UserDto userDto, User user) {
        if (userDto.getRole() == null || userDto.getRole().equalsIgnoreCase(String.valueOf(Role.TRAVELLER))) {
            user.setRole(Role.TRAVELLER);
        } else if (userDto.getRole().equalsIgnoreCase(String.valueOf(Role.ADMIN))) {
            user.setRole(Role.ADMIN);
        }
    }

    @Override
    public UserProfileResponse createUser(UserDto userDto) throws UserException, IOException {

        boolean isValidEmail = appUtils.validEmail(userDto.getEmail());
        if (!isValidEmail) {
            throw new InvalidEmailException("Email is invalid");
        }
        checkIfUserExist(userDto.getEmail());

        if (userDto.getPassword() == null || userDto.getPassword().equals("")) {
            throw new PasswordMismatchException("Password can not empty");
        }
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        LocalDate dateOfBirth = appUtils.createLocalDate(userDto.getDate());

        User createdUser = createUserFromDto(userDto, encodedPassword);
        createdUser.setDateOfBirth(dateOfBirth);

        String token = JwtTokenUtils.generateEmailVerificationToken(createdUser.getEmail());
        createdUser.setTokenForEmail(token);

        String otp = String.valueOf(appUtils.generateOTP());
        createdUser.setValidOTP(passwordEncoder.encode(otp));

        createdUser.setNinId(null);
        createdUser.setKycVerified(false);

        String url = "http://" + request.getServerName() + ":8080" + "/api/v1/verify-email?token="
                + token + "&email=" + userDto.getEmail();


        String email = createdUser.getEmail();
        String subject = "Unsolo: Verify Profile";
        String body =
                "<html> " +
                        "<body>" +
                        "<h4>Hi " + createdUser.getFirstName() + " " + createdUser.getLastName() + ",</h4> \n" +
                        "<p>Welcome to Unsolo.\n" +
                        "To activate your Unsolo Account, enter your OTP" +
                        "Your otp is " + otp + "\n" +
                        "<a href=" + url + ">verify here</a></p>" +
                        "</body> " +
                        "</html>";
        emailService.sendMail(email, subject, body, "text/html");

        if (createdUser instanceof Traveller) {
            createdUser.setRole(Role.TRAVELLER);
            travellerRepository.save((Traveller) createdUser);
        }

        if (createdUser instanceof Admin) {
            createdUser.setRole(Role.ADMIN);
            adminRepository.save((Admin) createdUser);
        }

        return UserProfileResponse.builder()
                .firstName(createdUser.getFirstName())
                .lastName(createdUser.getLastName())
                .email(createdUser.getEmail())
                .phoneNumber(createdUser.getPhoneNumber())
                .gender(String.valueOf(createdUser.getGender()))
                .build();
    }

    private User createUserFromDto(UserDto userDto, String encodedPassword) {
        User user;

        if (userDto.getRole() == null) {
            user = new Traveller();
        } else if (userDto.getRole().equalsIgnoreCase(String.valueOf(Role.ADMIN))) {
            user = new Admin();
        } else {
            user = new Traveller();
        }

        BeanUtils.copyProperties(userDto, user);
        user.setPassword(encodedPassword);
        user.setGender(Gender.valueOf(userDto.getGender().toUpperCase()));
        assignRole(userDto, user);
        return user;
    }

    @Override
    public String verifyOTP(OTPRequest otpRequest) throws UserNotFoundException {
        Optional<User> userOptional;

        userOptional = adminRepository.findByEmail(otpRequest.getEmailForOTP());
        if (userOptional.isEmpty()) {
            userOptional = travellerRepository.findByEmail(otpRequest.getEmailForOTP());
        }
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        if (user.isVerified()) {
            return "This account is already verified";
        }

         if (passwordEncoder.matches(otpRequest.getOtp(), user.getValidOTP())) {
            user.setVerified(true);
            if (user.getRole().equals(Role.ADMIN)) {
                adminRepository.save((Admin) user);
            } else {
                travellerRepository.save((Traveller) user);
            }
            return "Verification successful";
        }
         else {
            return "Check the OTP and try again";
        }
    }

    private boolean confirmUserPasswords(String inputPassword, String storedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(inputPassword, storedPassword);
    }

    private void checkIfUserExist(String email) throws UserAlreadyExistException {
        Optional<User> userOptional;

        userOptional = adminRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            userOptional = travellerRepository.findByEmail(email);
        }

        if (userOptional.isPresent()) {
            throw new UserAlreadyExistException("User with this email already exists");
        }
    }

    private void confirmPasswords(String password1, String password2) throws PasswordMismatchException {
        if (!(password1.equals(password2))) {
            throw new PasswordMismatchException("Password mismatch");
        }
    }

    @Override
    public UserProfileResponse updateUserDetails(String email, UserUpdateRequest userUpdateRequest) throws UserNotFoundException {
        Optional<User> userOptional = adminRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            userOptional = travellerRepository.findByEmail(email);
        }

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }

        User user = userOptional.get();

        if (userUpdateRequest.getFirstName() != null) {
            user.setFirstName(userUpdateRequest.getFirstName());
        }

        if (userUpdateRequest.getLastName() != null) {
            user.setLastName(userUpdateRequest.getLastName());
        }

        if (userUpdateRequest.getEmail() != null) {
            user.setEmail(userUpdateRequest.getEmail());
        }

        if (userUpdateRequest.getGender() != null) {
            user.setGender(Gender.valueOf(userUpdateRequest.getGender().toUpperCase()));
        }

        if (userUpdateRequest.getLocation() != null) {
            user.setLocation(userUpdateRequest.getLocation());
        }

        if (userUpdateRequest.getDescription() != null) {
            user.setDescription(userUpdateRequest.getDescription());
        }

        if (userUpdateRequest.getProfilePicture() != null) {
            user.setProfilePicture(userUpdateRequest.getProfilePicture());
        }

        if (user.getRole().equals(Role.ADMIN)) {
            adminRepository.save((Admin) user);
        } else {
            travellerRepository.save((Traveller) user);
        }

        return UserProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .gender(user.getGender().toString())
                .location(user.getLocation())
                .description(user.getDescription())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    public DashboardResponse userDashboard(String email) throws UserNotFoundException {
        Optional<User> userOptional = adminRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            userOptional = travellerRepository.findByEmail(email);
        }

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }

        User user = userOptional.get();

        return DashboardResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .description(user.getDescription())
                .build();
    }
}