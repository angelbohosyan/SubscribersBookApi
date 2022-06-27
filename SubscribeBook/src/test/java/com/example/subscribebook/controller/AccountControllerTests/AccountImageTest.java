package com.example.subscribebook.controller.AccountControllerTests;

import com.example.subscribebook.models.Base64Json;
import com.example.subscribebook.repositories.ProfilePictureRepository;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.services.AccountService;
import com.example.subscribebook.util.JwtTokenUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountImageTest {

    @LocalServerPort
    private int port;


    private final String base64Image= "iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAIAAADTED8xAAADMElEQVR4nOzVwQnAIBQFQYXff81RUkQCOyDj1YOP" +
            "nbXWPmeTRef+/3O/OyBjzh3CD95BfqICMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0C" +
            "MK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CM" +
            "K0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CM" +
            "K0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CM" +
            "K0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CM" +
            "K0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CM" +
            "K0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CM" +
            "K0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK" +
            "0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0C" +
            "MK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMO0TAAD//2Anhf4QtqobAAAAAElFTkSuQmCC";

    @Autowired
    private TestRestTemplate restTemplate;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProfilePictureRepository profilePictureRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void uploadImage_pass() throws JSONException {
        JSONObject image = new JSONObject();
        image.put("base64String",base64Image);
        String testMapping = "image";
        Base64Json base64Json = new Base64Json();
        base64Json.setBase64String(base64Image);
        HttpHeaders headers = new HttpHeaders();
        String token =jwtTokenUtil.generateToken(new User("angelcho3","parola za angelcho3",new ArrayList<>()));
        headers.set("authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(image.toString(),headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/" + testMapping,
                HttpMethod.POST,
                httpEntity,
                String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }


    @Test
    public void getImage_pass() throws JSONException {
        String testMapping = "image/";
        HttpHeaders headers = new HttpHeaders();
        String token =jwtTokenUtil.generateToken(new User("angelcho3","parola za angelcho3",new ArrayList<>()));
        headers.set("authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/" + testMapping + "angelcho3",
                HttpMethod.GET,
                httpEntity,
                String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
