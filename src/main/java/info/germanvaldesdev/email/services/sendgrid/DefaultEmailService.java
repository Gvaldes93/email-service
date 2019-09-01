package info.germanvaldesdev.email.services.sendgrid;

import com.sendgrid.*;
import info.germanvaldesdev.email.services.EmailService;
import info.germanvaldesdev.email.services.EmailRequest;
import info.germanvaldesdev.email.services.EmailResponse;
import info.germanvaldesdev.email.services.QuoteOfDayService;
import info.germanvaldesdev.email.utils.HtmlValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;

@Service
public class DefaultEmailService implements EmailService {

    @Autowired
    private SendGridFactory sendGridFactory;

    @Autowired
    private QuoteOfDayService quoteOfDayService;


    @Override
    public EmailResponse sendEmail(EmailRequest emailRequest, boolean enrich) throws IllegalArgumentException, IOException {
        Assert.notNull(emailRequest, "EmailRequest object can not be null");
        Assert.notNull(emailRequest.getRecipients(), "There should be at least one valid recipient");

        Mail mail = buildEmail(emailRequest, enrich);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sendGridFactory.getInstance().api(request);

        return new EmailResponse(response.getStatusCode(), response.getBody());
    }

    private Mail buildEmail(EmailRequest emailRequest, boolean enrich) throws IOException {
        Email fromEmail = new Email();
        fromEmail.setName("Email Service");
        fromEmail.setEmail("help@mycompany.com");
        Mail mail = new Mail();
        mail.setFrom(fromEmail);

        appendBody(emailRequest, mail, enrich);
        Personalization personalization = new Personalization();
        appendRecipients(emailRequest, personalization);
        personalization.setSubject(emailRequest.getSubject());
        mail.addPersonalization(personalization);

        return mail;
    }

    private void appendBody(EmailRequest emailRequest, Mail mail, boolean enrich) throws IOException {
        if (emailRequest.getBody() == null) {
            return;
        }

        String body = emailRequest.getBody();
        String contentType = "text/plain";

        if (HtmlValidator.isHtml(body)) {
            contentType = "text/html";
        }

        if (enrich) {
            String qod = quoteOfDayService.getQuoteOfTheDay();
            if (body.contains("</body>")) {
                body = body.replace("</body>", "<p>" + qod + "</p></body>");
            } else {
                body += "\n" + qod;
            }
        }

        Content content = new Content(contentType, body);
        mail.addContent(content);
    }

    private void appendRecipients(EmailRequest emailRequest, Personalization personalization) {
        emailRequest.getRecipients().forEach(recipient -> {
            validateEmailAddress(recipient, "to");
            personalization.addTo(new Email(recipient));
        });

        if (!CollectionUtils.isEmpty(emailRequest.getCarbonCopies())) {
            emailRequest.getCarbonCopies().forEach(cc -> {
                validateEmailAddress(cc, "cc");
                personalization.addCc(new Email(cc));
            });
        }

        if (!CollectionUtils.isEmpty(emailRequest.getBlindCarbonCopies())) {
            emailRequest.getBlindCarbonCopies().forEach(bcc -> {
                validateEmailAddress(bcc, "bcc");
                personalization.addBcc(new Email(bcc));
            });
        }
    }

    private void validateEmailAddress(String recipient, String field) {
        if (!EmailValidator.getInstance().isValid(recipient)) {
            throw new IllegalArgumentException(String.format("Invalid email format in [%s] field, email [%s]", field, recipient));
        }
    }
}
