package info.germanvaldesdev.email.services.sendgrid;

import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import info.germanvaldesdev.email.services.EmailRequest;
import info.germanvaldesdev.email.services.EmailResponse;
import info.germanvaldesdev.email.services.QuoteOfDayService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultEmailServiceTest {

    @Mock
    private SendGridFactory sendGridFactory;

    @Mock
    private SendGrid sendGrid;

    @Mock
    private QuoteOfDayService quoteOfDayService;

    @InjectMocks
    private DefaultEmailService emailService;

    @Before
    public void setup() {
        when(sendGridFactory.getInstance()).thenReturn(sendGrid);
    }

    @Test
    public void shouldAllowSendingEmailGivenARecipient() throws IOException {
        EmailRequest emailRequest = new EmailRequest(Collections.singletonList("test@mailinator.com"));
        emailRequest.setBody("This is a test email");
        emailRequest.setSubject("Open this email");

        Response sendgridResponse = new Response();
        sendgridResponse.setBody("Email sent");
        sendgridResponse.setStatusCode(200);
        when(sendGrid.api(any(Request.class))).thenReturn(sendgridResponse);

        EmailResponse emailResponse = this.emailService.sendEmail(emailRequest, false);

        assertEquals("Email sent", emailResponse.getResponse());
        assertEquals(200, emailResponse.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullEmailRequest() throws IOException {
        this.emailService.sendEmail(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullRecipient() throws IOException {
        this.emailService.sendEmail(new EmailRequest(null), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldPreventSendEmailToInvalidEmailAddresses() throws IOException {
        EmailRequest emailRequest = new EmailRequest(Collections.singletonList("test123.com"));
        this.emailService.sendEmail(emailRequest, false);
    }


    @Test
    public void shouldSupportBodyAsHtml() throws IOException {
        EmailRequest emailRequest = new EmailRequest(Collections.singletonList("test@mailinator.com"));
        emailRequest.setBody("<h2>This is a test email</h2>");
        emailRequest.setSubject("Open this email");

        Response sendgridResponse = new Response();
        sendgridResponse.setBody("Email sent");
        sendgridResponse.setStatusCode(200);

        when(sendGrid.api(any(Request.class))).thenReturn(sendgridResponse);
        this.emailService.sendEmail(emailRequest, false);

        ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
        verify(sendGrid).api(argument.capture());

        assertTrue(argument.getValue().getBody().contains("\"content\":[{\"type\":\"text/html\",\"value\":\"<h2>This is a test email</h2>\""));
    }

    @Test
    public void shouldAddQuoteOfDayToHTMLBody() throws IOException {
        EmailRequest emailRequest = new EmailRequest(Collections.singletonList("test@mailinator.com"));
        emailRequest.setBody("<body> <p>This is a test email</p> </body>");
        emailRequest.setSubject("Open this email");

        Response sendgridResponse = new Response();
        sendgridResponse.setBody("Email sent");
        sendgridResponse.setStatusCode(200);

        when(quoteOfDayService.getQuoteOfTheDay()).thenReturn("There's no angry way to say bubbles");
        when(sendGrid.api(any(Request.class))).thenReturn(sendgridResponse);
        this.emailService.sendEmail(emailRequest, true);
        ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
        verify(sendGrid).api(argument.capture());

        assertTrue(argument.getValue().getBody().contains("<body> <p>This is a test email</p> There's no angry way to say bubbles </body>"));
    }

    @Test
    public void shouldAddQuoteOfDayToTextPlainBody() throws IOException {
        EmailRequest emailRequest = new EmailRequest(Collections.singletonList("test@mailinator.com"));
        emailRequest.setBody("This is a test email");
        emailRequest.setSubject("Open this email");

        Response sendgridResponse = new Response();
        sendgridResponse.setBody("Email sent");
        sendgridResponse.setStatusCode(200);

        when(quoteOfDayService.getQuoteOfTheDay()).thenReturn("There's no angry way to say bubbles");
        when(sendGrid.api(any(Request.class))).thenReturn(sendgridResponse);
        this.emailService.sendEmail(emailRequest, true);
        ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
        verify(sendGrid).api(argument.capture());

        assertTrue(argument.getValue().getBody().contains("This is a test email\\nThere's no angry way to say bubbles"));
    }

}