package info.germanvaldesdev.email.services;

import java.io.IOException;

public interface EmailService {

    EmailResponse sendEmail(EmailRequest emailRequest, boolean enrich) throws IllegalArgumentException, IOException;
}
