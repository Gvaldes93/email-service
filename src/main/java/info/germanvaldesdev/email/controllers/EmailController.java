package info.germanvaldesdev.email.controllers;

import info.germanvaldesdev.email.services.EmailRequest;
import info.germanvaldesdev.email.services.EmailResponse;
import info.germanvaldesdev.email.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("email")
public class EmailController {
    private static final Logger LOGGER = Logger.getLogger(EmailController.class.getName());

    @Autowired
    EmailService emailService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public String getTestData(@Valid @RequestBody EmailDto email,
                              @RequestParam(defaultValue = "false", required = false) boolean enrich) {

        EmailRequest emailRequest = new EmailRequest(email.getTo());
        emailRequest.setSubject(email.getSubject());
        emailRequest.setBody(email.getBody());
        emailRequest.setCarbonCopies(email.getCc());
        emailRequest.setBlindCarbonCopies(email.getBcc());

        try {
            EmailResponse emailResponse = emailService.sendEmail(emailRequest, enrich);

            if (emailResponse.getStatus() >= 400) {
                throw new ResponseStatusException(
                        HttpStatus.valueOf(emailResponse.getStatus()), emailResponse.getResponse()
                );
            }
            return enrich ? "Email sent successfully with quote of day" : "Email sent successfully";
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage()
                );
            }

            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong, try again"
            );
        }

    }

}
