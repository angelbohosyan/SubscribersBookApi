package com.example.subscribebook.controller.AccountControllerTests;

import com.example.subscribebook.models.Password;
import com.example.subscribebook.models.User;
import com.example.subscribebook.models.UserName;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.repositories.UserSaltRepository;
import com.example.subscribebook.services.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.ArrayList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ForgottenPasswordTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MailService mailService;

        @Autowired
        private UserRepository userRepository;

    @Autowired
    private UserSaltRepository userSaltRepository;


    @Test
    public void forgottenPassword_pass() {
        String testMapping = "/forgottenPassword";
        UserName userName = new UserName();
        userName.setName("angelcho3");
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/" + testMapping,
                userName,
                String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void refreshPassword_pass() {
        String testMapping = "/refreshPassword/";
        UserName userName = new UserName();
        userName.setName("angelcho3");
        User user = userRepository.getUserWithName(userName.getName());
        String password = user.getPassword();
        Password newPassword = new Password();
        newPassword.setPassword("parola za angelcho3");
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/" + testMapping + password,
                userName,
                String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

}
