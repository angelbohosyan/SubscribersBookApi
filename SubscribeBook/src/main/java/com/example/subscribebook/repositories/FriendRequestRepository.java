package com.example.subscribebook.repositories;

public interface FriendRequestRepository {

    void createFriendRequest(int from,int to);

    boolean getFriendRequestFromTo(int from,int to);

    void deleteFriendsRequest(int from,int to);
}
