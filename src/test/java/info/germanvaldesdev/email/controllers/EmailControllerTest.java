package info.germanvaldesdev.email.controllers;

import com.google.gson.Gson;
import info.germanvaldesdev.email.services.EmailRequest;
import info.germanvaldesdev.email.services.EmailResponse;
import info.germanvaldesdev.email.services.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(EmailController.class)
public class EmailControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmailService emailService;


    @Test
    public void sendEmailReturns200() throws Exception {
        when(emailService.sendEmail(any(EmailRequest.class), anyBoolean()))
                .thenReturn(new EmailResponse(200, "Success"));
        EmailDto email = new EmailDto();
        email.setTo(Collections.singletonList("test@email.com"));
        email.setBody("Email body");
        Gson gson = new Gson();
        String emailJson = gson.toJson(email);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/email").content(emailJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Email sent successfully", content);
    }


    @Test
    public void sendEmailWithQuoteOfDayReturns200AndCustomMessage() throws Exception {
        when(emailService.sendEmail(any(EmailRequest.class), anyBoolean()))
                .thenReturn(new EmailResponse(200, "Success"));

        EmailDto email = new EmailDto();
        email.setTo(Collections.singletonList("test@mailinator.com"));
        email.setBody("Email body");
        Gson gson = new Gson();
        String emailJson = gson.toJson(email);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/email").content(emailJson)
                .param("enrich", "true")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Email sent successfully with quote of day", content);
    }

    @Test
    public void sendingEmailToNullRecipientFailsWith400() throws Exception {
        String uri = "/email";
        EmailDto email = new EmailDto();
        email.setTo(null);
        Gson gson = new Gson();
        String emailJson = gson.toJson(email);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(emailJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }
}