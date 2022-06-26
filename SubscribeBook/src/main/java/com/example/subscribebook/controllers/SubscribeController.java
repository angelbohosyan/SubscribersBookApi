package com.example.subscribebook.controllers;

import com.example.subscribebook.models.UserName;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.services.SubscribeService;
import com.example.subscribebook.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class SubscribeController {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final SubscribeService subscribeService;


    public SubscribeController(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, SubscribeService subscribeService) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.subscribeService = subscribeService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody UserName user, @RequestHeader(name = "Authorization") String token) {
        Integer idFrom = jwtTokenUtil.extractIdWithBearer(token);
        System.out.println("tuk");
        return subscribeService.subscribe(idFrom,user.getName());
    }

    @DeleteMapping("/subscribe")
    public ResponseEntity<?> unSubscribe(@RequestBody UserName user, @RequestHeader(name = "Authorization") String token) {
        Integer idFrom = jwtTokenUtil.extractIdWithBearer(token);
        return subscribeService.unSubscribe(idFrom,user.getName());
    }
}
