package com.interswitch.Unsolorockets.utils;

import com.interswitch.Unsolorockets.exceptions.UserException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;

@Service
public class AppUtils {

    public Long generateOTP() {
        Random rnd = new Random();
        Long number = (long) rnd.nextInt(999999);
        return number;
    }

    public boolean validEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    public static boolean validateNinId(String ninId) {
        String regex = "\\d{11}";
        return ninId != null && ninId.matches(regex);
    }

    public LocalDate createLocalDate(String dateStr) {
        if (dateStr != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                throw new UserException("Invalid date format. Please use dd/MM/yyyy format.", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new UserException("Date cannot be null", HttpStatus.BAD_REQUEST);
        }
    }

    public static String generateWalletId(){
       return RandomStringUtils.randomNumeric(10);
    }

}
