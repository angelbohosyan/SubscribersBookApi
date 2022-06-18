package com.example.subscribebook.repositories;

import com.example.subscribebook.models.UrlWithUrl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface NewResultsForUrlWithUrlRepository {

    void createUrlUrlResults(String url, Integer url_id);

    void createMultipleUrlUrlResults(HashMap<String, HashSet<String>> map);

    List<UrlWithUrl> getUrlUrlResultsWithUrl(List<String> urls);

    List<UrlWithUrl> getAllUrlUrlResultsWithUrl();

    boolean exist(UrlWithUrl urlWithUrl);
}
