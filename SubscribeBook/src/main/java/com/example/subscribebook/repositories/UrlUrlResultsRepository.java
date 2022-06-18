package com.example.subscribebook.repositories;

import com.example.subscribebook.models.UrlWithUrl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlUrlResultsRepository {
    void createUrlUrlResults(String url, Integer url_id);

    void createMultipleUrlUrlResults(List<String> urls, Integer url_id);

    List<UrlWithUrl> getUrlUrlResultsWithUrl();

    void deleteUrlUrlResult(List<Integer> toList);
}
