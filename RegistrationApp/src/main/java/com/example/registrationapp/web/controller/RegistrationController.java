package com.example.registrationapp.web.controller;

import com.example.registrationapp.listenevent.RegistrationEvent;
import com.example.registrationapp.persistence.model.User;
import com.example.registrationapp.persistence.model.UserType;
import com.example.registrationapp.persistence.model.VerificationToken;
import com.example.registrationapp.plugin.SendEmail;
import com.example.registrationapp.service.IUserService;
import com.example.registrationapp.service.IUserTypeService;
import com.example.registrationapp.service.IVerificationTokenService;
import com.example.registrationapp.web.dto.UserDto;
import com.example.registrationapp.web.exception.SalaryInvalidateException;
import com.example.registrationapp.web.exception.UserAlreadyExistException;
import com.example.registrationapp.web.exception.UserNotFoundException;
import com.example.registrationapp.web.util.GenericResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserTypeService userTypeService;

    @Autowired
    private IVerificationTokenService verificationTokenService;

    public RegistrationController() {
        super();
    }

    @ApiOperation(value = "Registration new account", notes = "Returns a new account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 400, message = "Bad request - Error params"),
            @ApiResponse(code = 500, message = "Error Server - Error Error Server")
    })
    @PostMapping("/user/registration")
    public GenericResponse<UserDto> registerNewAccount(@RequestBody @Valid final UserDto userDto, final HttpServletRequest request){
        final User user = userService.registerNewAccount(userDto);
        SendEmail sendEmail = new SendEmail();
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        RegistrationEvent event = new RegistrationEvent(appUrl, request.getLocale(),user);
        sendEmail.sendConfirm(event, "Registration Confirmation");
        userDto.setUserType(user.getUserType().getTypeClassify());
        userDto.setUsername(user.getUsername());
        userDto.setSalary(user.getSalary());
        userDto.setPassword(user.getPassword());
        userDto.setId(user.getId());
        return new GenericResponse<>("Successfully", userDto, null);

    }
    @ApiOperation(value = "Confirm registration new account", notes = "Returns a message")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 400, message = "Bad request - Token error"),
            @ApiResponse(code = 403, message = "Permission Denied - Token error"),
            @ApiResponse(code = 500, message = "Error Server - Error Error Server")
    })
    @PostMapping("/registration/confirm")
    public  GenericResponse<String> confirmRegistrationAccount(@RequestParam("token") String token, final HttpServletRequest request){
        String result = userService.validateVerificationToken(token);
        if (result.equals("valid")){
            VerificationToken verificationToken = verificationTokenService.findByToken(token);
            SendEmail sendEmail = new SendEmail();
            String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            RegistrationEvent event = new RegistrationEvent(appUrl, request.getLocale(), verificationToken.getUser());
            sendEmail.sendConfirmSuccess(event, "Confirmation successfully");
            return new GenericResponse<>("Successfully");
        }
        return new GenericResponse<>("Failure");
    }
    @ApiOperation(value = "List Users", notes = "Returns list user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 400, message = "Bad request - Token error"),
            @ApiResponse(code = 403, message = "Permission Denied - Token error"),
            @ApiResponse(code = 500, message = "Error Server - Error Error Server")
    })
    @GetMapping("/users")
    public GenericResponse<UserDto> getUsers(){
        List<User> users = userService.getUsers();
        List<UserDto> userDtos = new ArrayList<>();
        for (User item: users) {
            UserDto userDto = new UserDto();
            userDto.setUserType(item.getUserType().getTypeClassify());
            userDto.setUsername(item.getUsername());
            userDto.setSalary(item.getSalary());
            userDto.setPassword(item.getPassword());
            userDto.setId(item.getId());
            userDtos.add(userDto);
        }
        return new GenericResponse<>("Successfully", userDtos, null);
    }
    @ApiOperation(value = "Get detail Users", notes = "Returns detail user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 400, message = "Bad request - Token error"),
            @ApiResponse(code = 403, message = "Permission Denied - Token error"),
            @ApiResponse(code = 500, message = "Error Server - Error Error Server")
    })
    @GetMapping("/users/{id}")
    public GenericResponse<UserDto> getUsers(@PathVariable long id){
        User user = userService.getUserByEnabledIsTrueAndId(id).orElseThrow(UserNotFoundException::new);
        UserDto userDto = new UserDto();
        userDto.setUserType(user.getUserType().getTypeClassify());
        userDto.setUsername(user.getUsername());
        userDto.setSalary(user.getSalary());
        userDto.setPassword(user.getPassword());
        userDto.setId(user.getId());
        return new GenericResponse<>("Successfully", userDto, null);
    }
    @ApiOperation(value = "Update detail Users", notes = "Returns detail user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 400, message = "Bad request - Token error"),
            @ApiResponse(code = 403, message = "Permission Denied - Token error"),
            @ApiResponse(code = 500, message = "Error Server - Error Error Server")
    })
    @PutMapping("/users/{id}")
    public GenericResponse<UserDto> updateUser(@PathVariable long id, @RequestBody UserDto userDto){
        User updateUser = userService.getUserByEnabledIsTrueAndId(id).orElseThrow(UserNotFoundException::new);
        long salary = userDto.getSalary();
        String username = userDto.getUsername();

        if (salary != 0){
            UserType userType = userTypeService.getUserTypeBySalary(salary);
            if (userType == null){
                throw new SalaryInvalidateException();
            }
            updateUser.setUserType(userType);
            updateUser.setSalary(salary);
        }

        if (username != null){
            User user = userService.getUserByUsernameAndNotId(username, updateUser.getId());
            if (user != null){
                throw new UserAlreadyExistException();
            }
            updateUser.setUsername(username);
        }
        try{
            updateUser = userService.updateUser(updateUser);
            UserDto userDtoDb = new UserDto();
            userDtoDb.setUserType(updateUser.getUserType().getTypeClassify());
            userDtoDb.setUsername(updateUser.getUsername());
            userDtoDb.setSalary(updateUser.getSalary());
            return new GenericResponse<>("Successfully", userDtoDb, null);
        }
        catch (Exception e){
            return new GenericResponse<>("Failure");
        }
    }
}
