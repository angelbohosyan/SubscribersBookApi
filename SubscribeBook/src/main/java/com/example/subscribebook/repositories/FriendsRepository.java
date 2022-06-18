package com.example.subscribebook.repositories;

import java.util.List;

public interface FriendsRepository {

    void createFriends(int user_id_1,int user_id_2);

    List<Integer> getFriends(int user_id);

    boolean isFriends(int user_id_1,int user_id_2);

    void deleteFriends(int user_id_1,int user_id_2);

}
