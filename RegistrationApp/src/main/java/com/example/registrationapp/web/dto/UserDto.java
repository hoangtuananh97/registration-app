package com.example.registrationapp.web.dto;

import com.example.registrationapp.validation.PasswordMatches;
import com.example.registrationapp.validation.ValidEmail;
import com.example.registrationapp.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@PasswordMatches
public class UserDto {
    @ValidEmail
    @NotNull
    @Size(min = 1, message = "{Size.userDto.email}")
    private String username;

    @ValidPassword
    @NotNull
    private String password;

    @NotNull
    @Range(min=0, message = "{Size.userDto.salary}")
    private Long salary;

}
