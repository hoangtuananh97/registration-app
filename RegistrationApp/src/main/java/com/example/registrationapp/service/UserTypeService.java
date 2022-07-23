package com.example.registrationapp.service;

import com.example.registrationapp.persistence.dao.UserTypeRepository;
import com.example.registrationapp.persistence.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserTypeService implements IUserTypeService{
    @Autowired
    private UserTypeRepository userTypeRepository;

    @Override
    public UserType getUserTypeBySalary(Long salary) {
        return userTypeRepository.getUserTypeBySalary(salary);
    }
}
