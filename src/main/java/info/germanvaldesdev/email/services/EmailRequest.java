package info.germanvaldesdev.email.services;

import java.util.List;

public class EmailRequest {

    private List<String> recipients;
    private List<String> carbonCopies;
    private List<String> blindCarbonCopies;
    private String body;
    private String subject;

    public EmailRequest(List<String> recipients) {
        this.recipients = recipients;
    }


    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public List<String> getCarbonCopies() {
        return carbonCopies;
    }

    public void setCarbonCopies(List<String> carbonCopies) {
        this.carbonCopies = carbonCopies;
    }

    public List<String> getBlindCarbonCopies() {
        return blindCarbonCopies;
    }

    public void setBlindCarbonCopies(List<String> blindCarbonCopies) {
        this.blindCarbonCopies = blindCarbonCopies;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
