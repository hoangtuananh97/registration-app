package com.example.registrationapp.listenevent;

import com.example.registrationapp.persistence.model.User;
import com.example.registrationapp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.UUID;

public class RegistrationListener implements ApplicationListener<RegistrationEvent> {

    @Autowired
    private IUserService userService;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private MessageSource messages;

    @Override
    public void onApplicationEvent(RegistrationEvent event) {
        this.confirmRegistrationEvent(event);
    }
    private void confirmRegistrationEvent(RegistrationEvent event){
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token);
        String recipientAddress = user.getUsername();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/registration/confirm?token=" + token;
        String message = messages.getMessage("message.regSuccess", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);

    }
}
