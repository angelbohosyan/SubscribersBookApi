package com.example.subscribebook.controller.AccountControllerTests;

import com.example.subscribebook.models.AuthenticationRequest;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.repositories.UserSaltRepository;
import com.example.subscribebook.util.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoginTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserSaltRepository userSaltRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private  UserDetailsService userDetailsService;

    final String testMapping = "login";

    @Test
    public void login_fails_NotFound() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("angel");
        authenticationRequest.setUsername("angel");
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/" + testMapping,
                authenticationRequest,String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void login_passes() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("vankata");
        authenticationRequest.setPassword("parola za vankata");
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/" + testMapping,
                authenticationRequest, String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
