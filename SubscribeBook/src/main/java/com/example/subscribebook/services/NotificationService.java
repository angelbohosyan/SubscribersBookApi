package com.example.subscribebook.services;

import com.example.subscribebook.models.PeopleNewResults;
import com.example.subscribebook.models.UrlWithUrl;
import com.example.subscribebook.repositories.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotificationService {

    private final SubscribeRepository subscribeRepository;

    private final UrlRepository urlRepository;

    private final UrlScopeRepository urlScopeRepository;

    private final FriendsRepository friendsRepository;

    private final NewResultsForUrlWithUrlRepository newResultsForUrlWithUrlRepository;

    private final SeenUrlsRepository seenUrlsRepository;

    public NotificationService(SubscribeRepository subscribeRepository, UrlRepository urlRepository, UrlScopeRepository urlScopeRepository, FriendsRepository friendsRepository, NewResultsForUrlWithUrlRepository newResultsForUrlWithUrlRepository, SeenUrlsRepository seenUrlsRepository) {
        this.subscribeRepository = subscribeRepository;
        this.urlRepository = urlRepository;
        this.urlScopeRepository = urlScopeRepository;
        this.friendsRepository = friendsRepository;
        this.newResultsForUrlWithUrlRepository = newResultsForUrlWithUrlRepository;
        this.seenUrlsRepository = seenUrlsRepository;
    }

    public List<PeopleNewResults> getFollowedPeopleNews(int id) {
        List<Integer> followedPeopleList = subscribeRepository.getSubscriptions(id);
        System.out.println(followedPeopleList.size());
        List<Integer> publicUrlsOfFollowedPeople = getPublicUrlsOfFollowedPeople(followedPeopleList);
        System.out.println(publicUrlsOfFollowedPeople.size());
        return urlRepository.getUsersIdById(publicUrlsOfFollowedPeople);
    }

    public List<PeopleNewResults> getPersonNews(int searchId) {
        List<Integer> publicUrlsOfFollowedPeople = getPublicUrlsOfFollowedPeople(new ArrayList<>(searchId));
        return urlRepository.getUsersIdById(publicUrlsOfFollowedPeople);
    }

    public List<Integer> getPublicUrlsOfFollowedPeople(List<Integer> followedPeopleList) {
        List<Integer> urlsOfFollowedPeople = urlRepository.getUrlsByUsersId(followedPeopleList);
        return urlScopeRepository.getUrlScopePublic(urlsOfFollowedPeople);
    }

    public List<PeopleNewResults> getFriendsNews(int id) {
        List<Integer> friends = friendsRepository.getFriends(id);
        List<Integer> urlsOfFriends = urlRepository.getUrlsByUsersId(friends);
        List<Integer> friendsUrls = urlScopeRepository.getUrlScopeFriend(urlsOfFriends);
        return urlRepository.getUsersIdById(friendsUrls);
    }

    public List<UrlWithUrl> getUserPersonalUrlUpdate(Integer id) {
        List<String> urls = urlRepository.getUrlsByUserId(id);
        List<UrlWithUrl> list = newResultsForUrlWithUrlRepository.getUrlUrlResultsWithUrl(urls);
        List<UrlWithUrl> result = new ArrayList<>();
        for (UrlWithUrl urlWithUrl :
                list) {
            if(!seenUrlsRepository.getSeenUrlByIdUrlAndParentUrl(id,urlWithUrl.getUrl(), urlWithUrl.getResultUrl())) {
                result.add(urlWithUrl);
            };
        }
        return result;
    }

    public List<PeopleNewResults> getFriendsNewsForName(Integer toId) {
        List<Integer> list = new ArrayList<>();
        list.add(toId);
        List<Integer> urlsOfFriends = urlRepository.getUrlsByUsersId(list);
        System.out.println(urlsOfFriends.size());
        List<Integer> friendsUrls = urlScopeRepository.getUrlScopeFriend(urlsOfFriends);
        System.out.println(friendsUrls.size());
        return urlRepository.getUsersIdById(friendsUrls);
    }
}
