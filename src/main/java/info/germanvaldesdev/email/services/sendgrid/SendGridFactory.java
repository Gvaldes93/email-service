package info.germanvaldesdev.email.services.sendgrid;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendGridFactory implements InitializingBean {
    private SendGrid sendGrid;

    @Value("${email.service.provider.api.key}")
    private String apiKey;

    @Override
    public void afterPropertiesSet() {
        this.sendGrid = new SendGrid(apiKey);
    }

    SendGrid getInstance() {
        return this.sendGrid;
    }
}
