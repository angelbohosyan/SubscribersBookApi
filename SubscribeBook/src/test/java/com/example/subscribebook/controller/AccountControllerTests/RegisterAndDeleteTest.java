package com.example.subscribebook.controller.AccountControllerTests;

import com.example.subscribebook.models.RegisterInput;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.repositories.UserSaltRepository;
import com.example.subscribebook.services.AccountService;
import com.example.subscribebook.services.MyUserDetailsService;
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
@AutoConfigureMockMvc
public class RegisterAndDeleteTest {

    @LocalServerPort
    private int port;

    final String testMapping = "register";
    final String nameThatExists = "arsenix";
    final String nameThatDoesntExistsBelowSixSymbols = "arsen";
    final String nameThatDoesntExists = "arsenix2";
    final String password = "arsenix";
    final String emailThatIsNotValid = "emailIsNotValid";
    final String emailThatExists = "arsenix_96@abv.bg";
    final String emailThatDoesntExists = "arsenix_962@abv.bg";

    final String responseIfNameExists = "User with that name already exists";

    final String responseIfNameIsLessThanSixSymbols = "Name should be at least 6";

    final String responseIfUserWithThatMailAlreadyExists = "User with that email already exists";

    final String responseIfMailIsNotValid = "Email does not match ";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSaltRepository userSaltRepository;

    @Autowired
    private  AccountService accountService;

    @Autowired
    private  JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void register_fails_nameAlreadyExists() {
        RegisterInput registerInput = new RegisterInput();
        registerInput.setName(nameThatExists);
        registerInput.setPassword(password);
        registerInput.setEmail(emailThatDoesntExists);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/" + testMapping,
                registerInput,String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody(), responseIfNameExists);
    }

    @Test
    public void register_fails_nameIsLessThanSixSymbols() {
        RegisterInput registerInput = new RegisterInput();
        registerInput.setName(nameThatDoesntExistsBelowSixSymbols);
        registerInput.setPassword(password);
        registerInput.setEmail(emailThatDoesntExists);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/" + testMapping,
                registerInput,String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody(), responseIfNameIsLessThanSixSymbols);
    }

    @Test
    public void register_fails_emailAlreadyExists() {
        RegisterInput registerInput = new RegisterInput();
        registerInput.setName(nameThatDoesntExists);
        registerInput.setPassword(password);
        registerInput.setEmail(emailThatExists);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/" + testMapping,
                registerInput,String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody(), responseIfUserWithThatMailAlreadyExists);
    }

    @Test
    public void register_fails_emailIsNotValid() {
        RegisterInput registerInput = new RegisterInput();
        registerInput.setName(nameThatDoesntExists);
        registerInput.setPassword(password);
        registerInput.setEmail(emailThatIsNotValid);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/" + testMapping,
                registerInput,String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody(), responseIfMailIsNotValid);
    }

    @Test
    public void register_passes_inputIsValid() {
        RegisterInput registerInput = new RegisterInput();
        registerInput.setName(nameThatDoesntExists);
        registerInput.setPassword(password);
        registerInput.setEmail(emailThatDoesntExists);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/" + testMapping,
                registerInput,String.class);
        System.out.println(response.getBody());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        deleteUser(jwtTokenUtil.generateToken(new User(nameThatDoesntExists,password,new ArrayList<>())));
    }

    public void deleteUser(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Bearer " + token);
        restTemplate.exchange("http://localhost:" + port + "/" + "delete",
                HttpMethod.DELETE,new HttpEntity<>(headers),String.class);
    }
}
