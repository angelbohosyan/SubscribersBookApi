package com.example.subscribebook.controller.FriendControllerTest;

import com.example.subscribebook.repositories.FriendRequestRepository;
import com.example.subscribebook.repositories.FriendsRepository;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.services.FriendsService;
import com.example.subscribebook.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

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
    public void removeFriend_fails_notFound() {

    }
}
