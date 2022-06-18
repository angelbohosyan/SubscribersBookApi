package com.example.subscribebook.services;

import com.example.subscribebook.models.UserName;
import com.example.subscribebook.repositories.FriendRequestRepository;
import com.example.subscribebook.repositories.FriendsRepository;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public class FriendsService {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final FriendRequestRepository friendRequestRepository;

    private final FriendsRepository friendsRepository;

    public FriendsService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, FriendRequestRepository friendRequestRepository, FriendsRepository friendsRepository) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.friendRequestRepository = friendRequestRepository;
        this.friendsRepository = friendsRepository;
    }

    public ResponseEntity<?> sentRequest(String name,int idFrom) {
        Integer idTo = userRepository.getUserWithName(name).getId();
        if(friendRequestRepository.getFriendRequestFromTo(idFrom,idTo)) return ResponseEntity.badRequest().body("There is already a request");
        if(friendRequestRepository.getFriendRequestFromTo(idTo,idFrom)) {
            friendRequestRepository.deleteFriendsRequest(idTo,idFrom);
            friendsRepository.createFriends(idTo,idFrom);
            return ResponseEntity.ok().body("friends");
        } else {
            friendRequestRepository.createFriendRequest(idFrom,idTo);
            return ResponseEntity.ok().body("request send");
        }
    }

    public ResponseEntity<?> removeFriend(String  name,int idFrom) {
        Integer idTo = userRepository.getUserWithName(name).getId();
        friendsRepository.deleteFriends(idFrom,idTo);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> removeRequest(String name, Integer idFrom) {
        Integer idTo = userRepository.getUserWithName(name).getId();
        if(friendRequestRepository.getFriendRequestFromTo(idFrom,idTo))
            friendRequestRepository.deleteFriendsRequest(idFrom,idTo);
        else
            return ResponseEntity.badRequest().body("There is no suck friend request.");
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> createFriend(Integer idTo, Integer idFrom) {
        friendsRepository.createFriends(idTo,idFrom);
        return ResponseEntity.ok().build();
    }
}
