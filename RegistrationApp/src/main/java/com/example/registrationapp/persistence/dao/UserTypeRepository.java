package com.example.registrationapp.persistence.dao;

import com.example.registrationapp.persistence.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    @Query(
        value = "SELECT * FROM USER_TYPE WHERE (IS_LIMIT_SALARY = true AND FROM_SALARY < :salary AND :salary < TO_SALARY) OR (IS_LIMIT_SALARY = false AND FROM_SALARY < :salary)",
        nativeQuery = true
    )
    UserType getUserTypeBySalary(@Param("salary") Long salary);
}
