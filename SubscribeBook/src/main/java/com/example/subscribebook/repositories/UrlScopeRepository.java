package com.example.subscribebook.repositories;

import java.util.List;

public interface UrlScopeRepository {

    void createUrlScope(int urlId,String scope);

    void deleteUrlScope(int urlId);

    List<Integer> getUrlScopePublic(List<Integer> urlId);

    List<Integer> getUrlScopeFriend(List<Integer> urlId);

}
