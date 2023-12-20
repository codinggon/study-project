package com.gon.fitness.web.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

public interface EmailService {

    void sendEmail(EmailMessage emailMessage);


}
