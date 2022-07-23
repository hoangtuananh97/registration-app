package com.example.registrationapp.service;

import com.example.registrationapp.persistence.dao.VerificationTokenRepository;
import com.example.registrationapp.persistence.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class VerificationTokenService implements IVerificationTokenService{
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}
