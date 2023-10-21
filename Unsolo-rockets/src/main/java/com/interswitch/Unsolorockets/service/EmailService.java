package com.interswitch.Unsolorockets.service;

import java.io.IOException;

public interface EmailService {
    void sendMail(String receiverEmail, String subject, String emailBody, String contentType) throws IOException;
}
