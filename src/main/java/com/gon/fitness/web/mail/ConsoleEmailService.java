package com.gon.fitness.web.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
@Slf4j
public class ConsoleEmailService implements EmailService{


    @Override
    public void sendEmail(EmailMessage emailMessage) {
       log.info("send email: {}",emailMessage.getMessage());
    }

}
