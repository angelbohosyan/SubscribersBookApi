package com.example.subscribebook.repositories;

import java.util.List;

public interface SubscribeRepository {

    void createSubscription(int from,int to);

    List<Integer> getSubscriptions(Integer id);

    void deleteSubscription(Integer idFrom, Integer idTo);
}
