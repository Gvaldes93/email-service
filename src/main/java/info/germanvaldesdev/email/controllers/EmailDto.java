package info.germanvaldesdev.email.controllers;

import javax.validation.constraints.NotNull;
import java.util.List;

public class EmailDto {
    @NotNull
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String body;
    private String subject;

    List<String> getTo() {
        return to;
    }

    void setTo(List<String> to) {
        this.to = to;
    }

    List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    String getBody() {
        return body;
    }

    void setBody(String body) {
        this.body = body;
    }

    String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
