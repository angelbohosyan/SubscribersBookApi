package com.example.subscribebook.controllers;

import com.example.subscribebook.exceptions.UserRepositoryExceptions.GetUserWithNameException;
import com.example.subscribebook.models.MakePostPublicRequest;
import com.example.subscribebook.models.UrlDAO;
import com.example.subscribebook.models.UrlRequest;
import com.example.subscribebook.models.UrlWithUrl;
import com.example.subscribebook.rabbit.config.MessagingConfig;
import com.example.subscribebook.repositories.*;
import com.example.subscribebook.services.GoogleApiService;
import com.example.subscribebook.services.GoogleUrlService;
import com.example.subscribebook.util.JwtTokenUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@EnableScheduling
public class GoogleUrlController {

    @Autowired
    private  RabbitTemplate rabbitTemplate;
    private final GoogleUrlService googleUrlService;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final NewResultsForUrlWithUrlRepository newResultsForUrlWithUrlRepository;

    private final UrlRepository urlRepository;

    private final UrlUrlResultsRepository urlUrlResultsRepository;

    private final UrlScopeRepository urlScopeRepository;

    private final SeenUrlsRepository seenUrlsRepository;

    public GoogleUrlController( GoogleUrlService googleUrlService, UserRepository userRepository, JwtTokenUtil jwtTokenUtil, NewResultsForUrlWithUrlRepository newResultsForUrlWithUrlRepository, UrlRepository urlRepository, UrlUrlResultsRepository urlUrlResultsRepository, UrlScopeRepository urlScopeRepository, SeenUrlsRepository seenUrlsRepository, GoogleApiService googleApiService) {
        this.googleUrlService = googleUrlService;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.newResultsForUrlWithUrlRepository = newResultsForUrlWithUrlRepository;
        this.urlRepository = urlRepository;
        this.urlUrlResultsRepository = urlUrlResultsRepository;
        this.urlScopeRepository = urlScopeRepository;
        this.seenUrlsRepository = seenUrlsRepository;
    }

    @PostMapping("/userFoundUrl")
    public ResponseEntity<?> userFoundUrl(@RequestBody UrlWithUrl urlWithUrl, @RequestHeader(name = "Authorization") String token) {
        System.out.println(urlWithUrl.getResultUrl() + " " + urlWithUrl.getUrl());
        Integer id = jwtTokenUtil.extractIdWithBearer(token);
        return googleUrlService.userFoundUrl(urlWithUrl,id);
    }

    @GetMapping("urls/{name}")
    public ResponseEntity<?> getProfile(@PathVariable String name) {
        try {
            Integer id = userRepository.getUserWithName(name).getId();
            return googleUrlService.getProfile(id);
        } catch (GetUserWithNameException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/url")
    public ResponseEntity<?> submitUrl(@RequestBody UrlRequest url, @RequestHeader(name = "Authorization") String token) throws IOException {
        Integer id = jwtTokenUtil.extractIdWithBearer(token);
        url.setUrl(url.getUrl().replace(' ','_'));
        UrlDAO urlDAO = urlRepository.createUrl(url.getUrl(), id);
        rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE,MessagingConfig.ROUTING_KEY,urlDAO);
        return ResponseEntity.ok().body(urlDAO.getId());
    }

    @GetMapping("/url/{urlId}")
    public ResponseEntity<?> getResult(@PathVariable Integer urlId, @RequestHeader(name = "Authorization") String token) {
        int userId = jwtTokenUtil.extractIdWithBearer(token);
        if(urlRepository.getUrlsByUserIdAndUrl(urlId, userId)) {
            return ResponseEntity.ok().body(urlUrlResultsRepository.getUrlUrlResultsWithUrlId(urlId));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Scheduled(fixedRate = 10000)
    public void scheduleFixedRateTask() throws IOException {

        HashMap<String, HashSet<String>> hashMap = new HashMap<>();
        List<String> urls = urlRepository.getUrls().stream().map(UrlDAO::getUrl).toList();
        for (String url :
                urls) {
//            hashMap.put(url,getGoogleLinks(url));
            hashMap.put(url, new HashSet<>((Collections.singleton("https://www.speedtest.net/"))));
        }
        List<UrlWithUrl> urlWithUrlList = urlUrlResultsRepository.getUrlUrlResultsWithUrl();

        for (UrlWithUrl urlWithUrl :
                urlWithUrlList) {
            hashMap.get(urlWithUrl.getUrl()).remove(urlWithUrl.getResultUrl());
        }

        for (UrlWithUrl urlWithUrl :
                newResultsForUrlWithUrlRepository.getAllUrlUrlResultsWithUrl()) {
            if(hashMap.containsKey(urlWithUrl.getResultUrl())&&
                    hashMap.get(urlWithUrl.getResultUrl()).contains((urlWithUrl.getUrl()))) {
                hashMap.get(urlWithUrl.getResultUrl()).remove(urlWithUrl.getUrl());
            }
        }

        if(hashMap.values().stream().mapToInt(HashSet::size).sum()>0) {
            newResultsForUrlWithUrlRepository.createMultipleUrlUrlResults(hashMap);
        }
    }

    @PostMapping("/makeUrlPublic")
    public ResponseEntity<?> makePostPublic(@RequestHeader(name = "Authorization") String token,@RequestBody MakePostPublicRequest urlId) {
        Integer id = jwtTokenUtil.extractIdWithBearer(token);
        if(urlRepository.getUserIdById(urlId.getUrlId()).intValue()==id.intValue()) {
            urlScopeRepository.deleteUrlScope(urlId.getUrlId());
            urlScopeRepository.createUrlScope(urlId.getUrlId(),"public");
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("The url doesn't belong to the user");
        }
    }

    @PostMapping("/makeUrlFriend")
    public ResponseEntity<?> makePostFriend(@RequestHeader(name = "Authorization") String token,@RequestBody MakePostPublicRequest urlId) {
        Integer id = jwtTokenUtil.extractIdWithBearer(token);
        if(urlRepository.getUserIdById(urlId.getUrlId()).intValue()==id.intValue()) {
            urlScopeRepository.deleteUrlScope(urlId.getUrlId());
            urlScopeRepository.createUrlScope(urlId.getUrlId(),"friend");
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("The url doesn't belong to the user");
        }
    }

    @PostMapping("/userNotFoundUrl")
    public ResponseEntity<?> userNotFoundUrl(@RequestBody UrlWithUrl urlWithUrl,@RequestHeader(name = "Authorization") String token) {
        System.out.println(urlWithUrl.getResultUrl() + " " + urlWithUrl.getUrl());
        Integer id = jwtTokenUtil.extractIdWithBearer(token);
        System.out.println("tuk");
        if(!newResultsForUrlWithUrlRepository.exist(urlWithUrl)) return ResponseEntity.notFound().build();
        System.out.println("tuk2");
        if(!urlRepository.exist(urlWithUrl.getResultUrl(),id)) return ResponseEntity.notFound().build();
        System.out.println("tuk3");
        if(!urlWithUrl.getUrl().equals("")) {
            urlUrlResultsRepository.createUrlUrlResults(urlWithUrl.getResultUrl(),urlRepository.getIdByUserIdAndUrl(id,urlWithUrl.getResultUrl()));
        }
        seenUrlsRepository.createSeenUrl(id,urlWithUrl.getUrl(), urlWithUrl.getResultUrl());
        return ResponseEntity.ok().build();
    }

}
