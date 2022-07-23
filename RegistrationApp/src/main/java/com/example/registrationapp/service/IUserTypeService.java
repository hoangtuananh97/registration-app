package com.example.registrationapp.service;

import com.example.registrationapp.persistence.model.UserType;

public interface IUserTypeService {
    UserType getUserTypeBySalary(Long salary);
}
