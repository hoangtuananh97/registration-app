package com.example.registrationapp.service;

import com.example.registrationapp.persistence.model.User;
import com.example.registrationapp.persistence.model.VerificationToken;
import com.example.registrationapp.web.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User registerNewAccount(UserDto userDto);
    void createVerificationTokenForUser(User user, String token);
    VerificationToken getVerificationToken(String VerificationToken);
    void saveRegisteredUser(User user);
    User getUser(String verificationToken);
    String validateVerificationToken(String token);
    List<User> getUsers();
    Optional<User> getUserById(long id);
    User getUserByUsernameAndNotId(String username, long id);
    User updateUser(User user);
}
