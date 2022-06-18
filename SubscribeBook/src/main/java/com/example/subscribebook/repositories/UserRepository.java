package com.example.subscribebook.repositories;

import com.example.subscribebook.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {
    User createUser(String name, String password , String email);
    User getUser(int id);

    User getUserWithName(String name);
    List<User> listUsers();

    boolean existsByUsername(String name);

    boolean existsByEmail(String email);

    List<String> getUsers(List<Integer> toList);

    void deleteUserWithName(int id);
}
