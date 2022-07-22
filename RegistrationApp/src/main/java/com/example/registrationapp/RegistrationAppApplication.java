package com.example.registrationapp;

import com.example.registrationapp.persistence.dao.UserTypeRepository;
import com.example.registrationapp.persistence.model.UserType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class RegistrationAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistrationAppApplication.class, args);
    }
    @Bean
    public CommandLineRunner mapDemo(UserTypeRepository userTypeRepository){
        return args -> {
            userTypeRepository.save(new UserType("SLIVER",15000,30000));
            userTypeRepository.save(new UserType("GOLD",30001,50000));
            UserType memberTypes = new UserType();
            memberTypes.setTypeClassify("PLATINUM");
            memberTypes.setFromSalary(50001);
            memberTypes.setLimitSalary(false);
            userTypeRepository.save(memberTypes);
        };
    }
}
