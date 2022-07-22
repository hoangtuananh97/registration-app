package com.example.registrationapp.plugin;

import com.example.registrationapp.listenevent.RegistrationEvent;
import com.example.registrationapp.persistence.model.User;
import com.example.registrationapp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.UUID;

public class SendEmail {
    @Autowired
    private IUserService userService;

    @Autowired
    private MailSender mailSender;

    public void sendConfirm(RegistrationEvent event){
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token);
        String recipientAddress = user.getUsername();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/api/v1/registration/confirm?token=" + token;
//        String message = messages.getMessage("message.regSuccess", null, event.getLocale());
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Registration Confirmation "+ confirmationUrl);
        mailSender.send(email);
    }
}
