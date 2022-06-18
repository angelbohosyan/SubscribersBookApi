package com.example.subscribebook.repositories;

import com.example.subscribebook.models.PeopleNewResults;
import com.example.subscribebook.models.UrlDAO;
import com.example.subscribebook.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepository {
    UrlDAO createUrl(String url, Integer user_id);

    List<UrlDAO> getUrls();

    List<String> getUrlsByUserId(Integer id);

    void deleteUrlsByUserIdAndUrlName(Integer id, String resultUrl);

    Integer getIdByUserIdAndUrl(Integer id, String resultUrl);

    boolean exist(String resultUrl, Integer id);

    List<Integer> getUrlsByUsersId(List<Integer> followedPeopleList);

    List<PeopleNewResults> getUsersIdById(List<Integer> publicUrlsOfFollowedPeople);

    Integer getUserIdById(Integer id);
}
