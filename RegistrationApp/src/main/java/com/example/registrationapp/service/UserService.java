package com.example.registrationapp.service;

import com.example.registrationapp.persistence.dao.UserRepository;
import com.example.registrationapp.persistence.dao.UserTypeRepository;
import com.example.registrationapp.persistence.dao.VerificationTokenRepository;
import com.example.registrationapp.persistence.model.User;
import com.example.registrationapp.persistence.model.UserType;
import com.example.registrationapp.persistence.model.VerificationToken;
import com.example.registrationapp.web.dto.UserDto;
import com.example.registrationapp.web.exception.SalaryInvalidateException;
import com.example.registrationapp.web.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    @Override
    public User registerNewAccount(UserDto userDto) {
        if (usernameExists(userDto.getUsername())){
            throw new UserAlreadyExistException("There is an account with that email address: " + userDto.getUsername());
        }
        UserType userType = userTypeRepository.getUserTypeBySalary((long) userDto.getSalary());
        if (userType == null){
            throw new SalaryInvalidateException("Minimum salary for new member is 15000");
        }
        final User user = new User();
        user.setUsername(userDto.getUsername());
        user.setSalary(userDto.getSalary());
        user.setUserType(userType);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return verificationTokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

    @Override
    public User getUser(final String verificationToken) {
        final VerificationToken token = verificationTokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0
        ) {
            verificationTokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        userRepository.save(user);
        return TOKEN_VALID;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findUsersByEnabledIsTrue();
    }

    @Override
    public Optional<User> getUserByEnabledIsTrueAndId(long id) {
        return Optional.ofNullable(userRepository.findUserByEnabledIsTrueAndId(id));
    }

    @Override
    public User getUserByUsernameAndNotId(String username, long id) {
        return userRepository.findByUsernameAndNotId(username, id);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public boolean usernameExists(String username){
        return userRepository.findByUsername(username) != null;
    }
}
