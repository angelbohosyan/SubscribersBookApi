package com.example.subscribebook.controller.FriendControllerTest;

import com.example.subscribebook.models.UserName;
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
public class SendAndRemoveFriendRequestTest {

    @LocalServerPort
    private int port;

    final String sendFriendRequestEndPoint = "sendFriendRequest";

    final String removeFriendRequestEndPoint = "removeFriendRequest";

    final String responseIfRequestIsToYourself = "You can not send friend request to yourself";

    final String responseIfRequestAlreadyExists = "There is already a request";

    final String responseIfRemoveRequestNotExists = "There is no suck friend request.";

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
    public void sentFriendRequest_fails_notAuthorized() {
        UserName userName = new UserName();
        userName.setName("arsenix");

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/" + sendFriendRequestEndPoint,
                userName,String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }
    @Test
    public void sentFriendRequest_fails_badRequest() throws JSONException {

        final String nameThatExists = "arsenix";
        final String password = "arsenix";

        String token = jwtTokenUtil.generateToken(new User(nameThatExists,password,new ArrayList<>()));

        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject userName = new JSONObject();
        userName.put("name",nameThatExists);

        HttpEntity<String> entity = new HttpEntity<>(userName.toString(),headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/" + sendFriendRequestEndPoint,
                HttpMethod.POST,entity,String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody(), responseIfRequestIsToYourself);
    }

    @Test
    public void sentFriendRequest_passes_then_again_fails_alreadyExists_than_removeFriendRequest_passes_then_again_fails_alreadyExists() throws JSONException {
        final String nameThatExists = "arsenix";
        final String password = "arsenix";

        String token = jwtTokenUtil.generateToken(new User(nameThatExists,password,new ArrayList<>()));

        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject userName = new JSONObject();
        userName.put("name","vankata");

        HttpEntity<String> entity = new HttpEntity<>(userName.toString(),headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/" + sendFriendRequestEndPoint,
                HttpMethod.POST,entity,String.class);
        System.out.println(response.getBody());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        response = restTemplate.exchange("http://localhost:" + port + "/" + sendFriendRequestEndPoint,
                HttpMethod.POST,entity,String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody(), responseIfRequestAlreadyExists);

        response = restTemplate.exchange("http://localhost:" + port + "/" + removeFriendRequestEndPoint,
                HttpMethod.POST,entity,String.class);
        System.out.println(response.getBody());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        response = restTemplate.exchange("http://localhost:" + port + "/" + removeFriendRequestEndPoint,
                HttpMethod.POST,entity,String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody(), responseIfRemoveRequestNotExists);
    }

    @Test
    public void sentRequest_fails_cantSendToYourself() throws JSONException {
        final String nameThatExists = "angelcho3";
        final String password = "parolza za angelcho3";

        String token = jwtTokenUtil.generateToken(new User(nameThatExists,password,new ArrayList<>()));

        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject userName = new JSONObject();
        userName.put("name","angelcho3");

        HttpEntity<String> entity = new HttpEntity<>(userName.toString(),headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/" + sendFriendRequestEndPoint,
                HttpMethod.POST,entity,String.class);
        System.out.println(response.getBody());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
