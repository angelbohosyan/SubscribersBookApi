package com.example.subscribebook.services;

import com.example.subscribebook.models.UrlWithUrl;
import com.example.subscribebook.repositories.NewResultsForUrlWithUrlRepository;
import com.example.subscribebook.repositories.UrlRepository;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class GoogleUrlService {

    private final NewResultsForUrlWithUrlRepository newResultsForUrlWithUrlRepository;

    private final UrlRepository urlRepository;
    private final NotificationService notificationService;

    public GoogleUrlService(NewResultsForUrlWithUrlRepository newResultsForUrlWithUrlRepository, UrlRepository urlRepository, NotificationService notificationService) {
        this.newResultsForUrlWithUrlRepository = newResultsForUrlWithUrlRepository;
        this.urlRepository = urlRepository;
        this.notificationService = notificationService;
    }

    public ResponseEntity<?> userFoundUrl(UrlWithUrl urlWithUrl,int id) {
        if(!newResultsForUrlWithUrlRepository.exist(urlWithUrl)) return ResponseEntity.notFound().build();
        if(!urlRepository.exist(urlWithUrl.getResultUrl(),id)) return ResponseEntity.notFound().build();
        urlRepository.deleteUrlsByUserIdAndUrlName(id,urlWithUrl.getResultUrl());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getProfile(Integer id) {
        List<Integer> publicUrlsOfFollowed = notificationService.getPublicUrlsOfFollowedPeople(new ArrayList<>(id));
        return ResponseEntity.ok().body(urlRepository.getUsersIdById(publicUrlsOfFollowed));
    }
}
