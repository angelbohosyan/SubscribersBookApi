package com.example.subscribebook.services;

import com.example.subscribebook.repositories.SubscribeRepository;
import com.example.subscribebook.repositories.UserRepository;
import org.springframework.http.ResponseEntity;

public class SubscribeService {

    private final UserRepository userRepository;

    private final SubscribeRepository subscribeRepository;

    public SubscribeService(UserRepository userRepository, SubscribeRepository subscribeRepository) {
        this.userRepository = userRepository;
        this.subscribeRepository = subscribeRepository;
    }

    public ResponseEntity<?> subscribe(int idFrom,String user) {
        Integer idTo = userRepository.getUserWithName(user).getId();
        subscribeRepository.createSubscription(idFrom,idTo);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> unSubscribe(Integer idFrom, String user) {
        Integer idTo = userRepository.getUserWithName(user).getId();
        subscribeRepository.deleteSubscription(idFrom,idTo);
        return ResponseEntity.ok().build();
    }
}
