package com.example.registrationapp.web.controller;

import com.example.registrationapp.listenevent.RegistrationEvent;
import com.example.registrationapp.persistence.model.User;
import com.example.registrationapp.plugin.SendEmail;
import com.example.registrationapp.service.IUserService;
import com.example.registrationapp.web.dto.UserDto;
import com.example.registrationapp.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {
    @Autowired
    private IUserService userService;


    public RegistrationController() {
        super();
    }

    @PostMapping("/user/registration")
    public GenericResponse registerNewAccount(@RequestBody @Valid final UserDto userDto, final HttpServletRequest request){
        final User user = userService.registerNewAccount(userDto);
        SendEmail sendEmail = new SendEmail();
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        RegistrationEvent event = new RegistrationEvent(appUrl, request.getLocale(),user);
        sendEmail.sendConfirm(event);
        return new GenericResponse("success");

    }
    @PostMapping("/registration/confirm")
    public  GenericResponse confirmRegistrationAccount(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if (result.equals("valid")){
            return new GenericResponse("success");
        }
        return new GenericResponse("failure");
    }
}
