package com.example.subscribebook.controller.AccountControllerTests;

import com.example.subscribebook.models.AuthenticationRequest;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.services.AccountService;
import com.example.subscribebook.util.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetProfileTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void getProfile_fails_notFound() {
        String testMapping = "account/";
        String userName = "vankata2";
        HttpHeaders headers = new HttpHeaders();
        String token =jwtTokenUtil.generateToken(new User("vankata","parola za vankata",new ArrayList<>()));
        headers.set("authorization", "Bearer " + token);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/" + testMapping + userName,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void getProfile_pass() {
        String testMapping = "account/";
        String userName = "vankata";
        HttpHeaders headers = new HttpHeaders();
        String token =jwtTokenUtil.generateToken(new User("vankata","parola za vankata",new ArrayList<>()));
        headers.set("authorization", "Bearer " + token);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/" + testMapping + userName,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

}
