package com.example.subscribebook.models;

import java.util.List;

public class Updates {
    List<UrlWithUrl> urlWithUrl;
    List<PeopleNewResults> peopleNewResults;

    List<PeopleNewResults> friendsNewResults;

    public List<PeopleNewResults> getFriendsNewResults() {
        return friendsNewResults;
    }

    public Updates(List<UrlWithUrl> urlWithUrl, List<PeopleNewResults> peopleNewResults, List<PeopleNewResults> friendsNewResults) {
        this.urlWithUrl = urlWithUrl;
        this.peopleNewResults = peopleNewResults;
        this.friendsNewResults = friendsNewResults;
    }

    public List<UrlWithUrl> getUrlWithUrl() {
        return urlWithUrl;
    }

    public List<PeopleNewResults> getFollowedPeopleNewResults() {
        return peopleNewResults;
    }
}
