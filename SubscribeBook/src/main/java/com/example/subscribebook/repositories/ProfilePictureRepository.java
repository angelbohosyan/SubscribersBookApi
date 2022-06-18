package com.example.subscribebook.repositories;

public interface ProfilePictureRepository {

    void createProfilePicture(int user_id,String path);

    void deleteProfilePicture(int user_id);

    String getPath(int user_id);

}
