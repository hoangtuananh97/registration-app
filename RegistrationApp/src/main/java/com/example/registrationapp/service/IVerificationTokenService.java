package com.example.registrationapp.service;

import com.example.registrationapp.persistence.model.VerificationToken;

public interface IVerificationTokenService {
    VerificationToken findByToken(String token);
}
