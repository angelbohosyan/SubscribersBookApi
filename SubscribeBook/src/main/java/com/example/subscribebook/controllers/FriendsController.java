package com.example.subscribebook.controllers;

import com.example.subscribebook.models.UserName;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.services.FriendsService;
import com.example.subscribebook.util.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class FriendsController {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final FriendsService friendsService;

    public FriendsController(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, FriendsService friendsService) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.friendsService = friendsService;
    }

    @PostMapping("/sendFriendRequest")
    public ResponseEntity<?> sentRequest(@RequestHeader(name = "Authorization") String token, @RequestBody UserName name) {
        if(userRepository.getUser(jwtTokenUtil.extractIdWithBearer(token)).getName().equals(name.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can not send friend request to yourself");
        }
        Integer idFrom = jwtTokenUtil.extractIdWithBearer(token);
        return friendsService.sentRequest(name.getName(),idFrom);
    }

    @DeleteMapping("/removeFriendRequest")
    public ResponseEntity<?> removeRequest(@RequestHeader(name = "Authorization") String token, @RequestBody UserName name) {
        if(userRepository.getUser(jwtTokenUtil.extractIdWithBearer(token)).getName().equals(name.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can not remove friend request from yourself");
        }
        Integer idFrom = jwtTokenUtil.extractIdWithBearer(token);
        return friendsService.removeRequest(name.getName(),idFrom);
    }

    @DeleteMapping("/removeFriend")
    public ResponseEntity<?> removeFriend(@RequestHeader(name = "Authorization") String token,@RequestBody UserName name) {
        Integer idFrom = jwtTokenUtil.extractIdWithBearer(token);
        return friendsService.removeFriend(name.getName(),idFrom);
    }

    @PostMapping("/createFriend")
    public ResponseEntity<?> createFriend(@RequestHeader(name = "Authorization") String token,@RequestBody UserName name) {
        Integer idFrom = jwtTokenUtil.extractIdWithBearer(token);
        Integer idTo = userRepository.getUserWithName(name.getName()).getId();
        return friendsService.createFriend(idTo,idFrom);
    }

}
