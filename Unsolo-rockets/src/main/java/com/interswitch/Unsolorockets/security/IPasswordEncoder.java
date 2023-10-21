package com.interswitch.Unsolorockets.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class IPasswordEncoder implements PasswordEncoder {

    private final AsymmetricEncryption asymmetricEncryption;

    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return bCryptPasswordEncoder().encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder().matches(rawPassword, encodedPassword) ||
                asymmetricEncryption.matches(rawPassword, encodedPassword);
    }

    public static boolean isBcryptEncoded(String encodedPassword){
        Pattern pattern = Pattern.compile("\\A\\$2([ayb])?\\$(\\d\\d)\\$[./\\dA-Za-z]{53}");
        return pattern.matcher(encodedPassword).matches();
    }


}