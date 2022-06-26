package com.example.subscribebook.controller.FriendControllerTest;

import com.example.subscribebook.models.Base64Json;
import com.example.subscribebook.repositories.FriendRequestRepository;
import com.example.subscribebook.repositories.FriendsRepository;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.services.FriendsService;
import com.example.subscribebook.util.JwtTokenUtil;
import org.json.JSONException;
import org.json.JSONObject;
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
public class FriendsTests {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendsService friendsService;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createFriend_pass_than_removeFriend_pass() throws JSONException {
        JSONObject image = new JSONObject();
        image.put("name","angelcho2");
        String testMapping = "createFriend";
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

        response = restTemplate.exchange(
                "http://localhost:" + port + "/" + "removeFriend",
                HttpMethod.DELETE,
                httpEntity,
                String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
