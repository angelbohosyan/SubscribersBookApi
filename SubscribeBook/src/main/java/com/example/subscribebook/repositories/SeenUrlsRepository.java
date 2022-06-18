package com.example.subscribebook.repositories;

public interface SeenUrlsRepository {
    void createSeenUrl(Integer user_id,String url,String parent_url);

    boolean getSeenUrlByIdUrlAndParentUrl(Integer user_id,String url,String parent_url);

}
