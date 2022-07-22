package com.example.registrationapp.service;

import com.example.registrationapp.persistence.model.User;
import com.example.registrationapp.persistence.model.VerificationToken;
import com.example.registrationapp.web.dto.UserDto;

public interface IUserService {
    User registerNewAccount(UserDto userDto);
    void createVerificationTokenForUser(User user, String token);
    VerificationToken getVerificationToken(String VerificationToken);
    void saveRegisteredUser(User user);
    User getUser(String verificationToken);
    String validateVerificationToken(String token);
}
