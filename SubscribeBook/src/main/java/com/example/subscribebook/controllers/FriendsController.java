package com.example.subscribebook.controllers;

import com.example.subscribebook.models.UserName;
import com.example.subscribebook.repositories.FriendRequestRepository;
import com.example.subscribebook.repositories.FriendsRepository;
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
        if(jwtTokenUtil.extractUsername(token.substring(7)).equals(name.getName())) {
            System.out.println("hi");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can not send friend request to yourself");
        }
        Integer idFrom = userRepository.getUserWithName(jwtTokenUtil.extractUsername(token.substring(7))).getId();
        return friendsService.sentRequest(name.getName(),idFrom);
    }

    @PostMapping("/removeFriendRequest")
    public ResponseEntity<?> removeRequest(@RequestHeader(name = "Authorization") String token, @RequestBody UserName name) {
        if(jwtTokenUtil.extractUsername(token.substring(7)).equals(name.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can not remove friend request from yourself");
        }
        Integer idFrom = userRepository.getUserWithName(jwtTokenUtil.extractUsername(token.substring(7))).getId();
        return friendsService.removeRequest(name.getName(),idFrom);
    }

    @DeleteMapping("/removeFriend")
    public ResponseEntity<?> removeFriend(@RequestHeader(name = "Authorization") String token,@RequestBody UserName name) {
        Integer idFrom = userRepository.getUserWithName(jwtTokenUtil.extractUsername(token.substring(7))).getId();
        return friendsService.removeFriend(name.getName(),idFrom);
    }

    @DeleteMapping("/createFriend")
    public ResponseEntity<?> createFriend(@RequestHeader(name = "Authorization") String token,@RequestBody UserName name) {
        Integer idFrom = userRepository.getUserWithName(jwtTokenUtil.extractUsername(token.substring(7))).getId();
        Integer idTo = userRepository.getUserWithName(jwtTokenUtil.extractUsername(name.getName())).getId();
        return friendsService.createFriend(idTo,idFrom);
    }

}
