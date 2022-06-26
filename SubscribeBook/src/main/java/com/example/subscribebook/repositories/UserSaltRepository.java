package com.example.subscribebook.repositories;

public interface UserSaltRepository {

    void createUserSalt(int user_id,String salt);

    String getSaltByUser(int user_id);

    void deleteUserSalt(int user_id);

    void refreshSalt(Integer id, String salt);
}
