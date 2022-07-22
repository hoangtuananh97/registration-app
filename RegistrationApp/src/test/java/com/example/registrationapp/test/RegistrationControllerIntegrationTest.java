package com.example.registrationapp.test;

import com.example.registrationapp.RegistrationAppApplication;
import com.example.registrationapp.persistence.dao.UserTypeRepository;
import com.example.registrationapp.persistence.model.User;
import com.example.registrationapp.persistence.model.UserType;
import com.example.registrationapp.persistence.model.VerificationToken;
import com.example.registrationapp.fixtures.TestDbConfig;
import com.example.registrationapp.fixtures.TestIntegrationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { RegistrationAppApplication.class, TestDbConfig.class, TestIntegrationConfig.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class RegistrationControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private Gson gson;
    @Autowired
    private UserTypeRepository userTypeRepository;
    private MockMvc mockMvc;
    private String token;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        User user = new User();
        user.setUsername(UUID.randomUUID().toString() + "@example.com");
        user.setPassword(UUID.randomUUID().toString());
        user.setSalary(1234567);
        UserType userType = userTypeRepository.getUserTypeBySalary((long)1234567);
        user.setUserType(userType);
        entityManager.persist(user);
        token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationToken.setExpiryDate(Date.from(Instant.now().plus(2, ChronoUnit.DAYS)));
        entityManager.persist(verificationToken);

        /*
            flush managed entities to the database to populate identifier field
         */
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testRegistrationConfirm() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post("/api/v1/registration/confirm?token=" + token));
        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("success")));;
    }


    @Test
    public void testRegistrationErrorSalary() throws Exception {

        User user = new User();
        user.setPassword("1bc@12mmmmAm1");
        user.setUsername("Abc@gmail.com");
        ResultActions resultActions = this.mockMvc.perform(post("/api/v1/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)));
        resultActions
                .andExpect(status().is(400))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("SalaryInvalidate")))
                .andExpect(jsonPath("$.message", containsString("Minimum salary for new member is 15000")));
    }

    @Test
    public void testRegistrationErrorUsername() throws Exception {

        User user = new User();
        user.setPassword("1bc12mmmmAm1");
        user.setUsername("Abc@gmail.com");
        user.setSalary(12345677);
        ResultActions resultActions = this.mockMvc.perform(post("/api/v1/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(user)));
        resultActions
                .andExpect(status().is(400))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("InvaliduserDto")))
                .andExpect(jsonPath("$.message", containsString("{\"field\":\"password\",\"defaultMessage\":\"Password must contain at least 1 special characters.\"}")));
    }
    @Test
    public void testRegistrationErrorPassword() throws Exception {
        List<String> passwords = new ArrayList<>();
        passwords.add("123"); // too short
        passwords.add("1abZRplYU");// no special character
        passwords.add("1_abidpsvl"); // no upper case letter
        passwords.add("abZRYUpl"); // no number
        passwords.add("1_abcZRYU"); // alphabet sequence
        passwords.add("1_abZRTYU"); // qwerty sequence
        passwords.add("123_zqrtU"); // numeric sequence
        User user = new User();
        user.setPassword("1bc@12mmmmAm1");
        user.setUsername("Abc@gmail.com");
        for (String password: passwords) {
            user.setPassword(password);
            ResultActions resultActions = this.mockMvc.perform(post("/api/v1/user/registration")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(user)));
            resultActions
                    .andExpect(status().is(400))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.error", is("InvaliduserDto")));
        }

    }
}
