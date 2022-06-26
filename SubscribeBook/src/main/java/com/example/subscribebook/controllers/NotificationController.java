package com.example.subscribebook.controllers;

import com.example.subscribebook.models.PeopleNewResults;
import com.example.subscribebook.models.Updates;
import com.example.subscribebook.repositories.*;
import com.example.subscribebook.services.NotificationService;
import com.example.subscribebook.util.JwtTokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class NotificationController {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    private final NotificationService notificationService;
    private final FriendsRepository friendRepository;

    public NotificationController(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, UrlRepository urlRepository, NewResultsForUrlWithUrlRepository newResultsForUrlWithUrlRepository, SeenUrlsRepository seenUrlsRepository, NotificationService notificationService, FriendsRepository friendRepository) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.notificationService = notificationService;
        this.friendRepository = friendRepository;
    }

    @GetMapping("/checkForUpdate")
    public Updates checkForUpdate(@RequestHeader(name = "Authorization") String token) throws MessagingException, IOException {
        Integer id = jwtTokenUtil.extractIdWithBearer(token);
        return new Updates(notificationService.getUserPersonalUrlUpdate(id),
                notificationService.getFollowedPeopleNews(id),
                notificationService.getFriendsNews(id));
    }

    @GetMapping("/checkForUpdate/{name}")
    public List<PeopleNewResults> checkForUpdateFromUser(@RequestHeader(name = "Authorization") String token, @PathVariable String name) throws MessagingException, IOException {
        Integer fromId = jwtTokenUtil.extractIdWithBearer(token);
        Integer toId = userRepository.getUserWithName(name).getId();
        if(friendRepository.isFriends(fromId,toId)) return notificationService.getFriendsNewsForName(toId);
        return notificationService.getPersonNews(toId);
    }
}
