package com.example.subscribebook.repositories.mysql;

import com.example.subscribebook.exceptions.UserRepositoryExceptions.*;
import com.example.subscribebook.mappers.SeenUrlMapper;
import com.example.subscribebook.mappers.UserMapper;
import com.example.subscribebook.models.User;
import com.example.subscribebook.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import static com.example.subscribebook.repositories.mysql.MySQLSeenUrlsRepository.Queries.GET_SEEN_URL_BY_ID_URL_AND_PARENT_URL;
import static com.example.subscribebook.repositories.mysql.MySQLUrlScopeRepository.Queries.GET_URL_SCOPE_PUBLIC;
import static com.example.subscribebook.repositories.mysql.MySQLUserRepository.Queries.*;

public class MySQLUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final TransactionTemplate txTemplate;

    public MySQLUserRepository(JdbcTemplate jdbcTemplate, TransactionTemplate txTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.txTemplate = txTemplate;
    }

    @Override
    public User createUser(String name, String password, String email) {
        try {
            return txTemplate.execute(status -> {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(
                            INSERT_USER, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, name);
                    ps.setString(2, password);
                    ps.setString(3, email);
                    return ps;
                }, keyHolder);
                int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
                return getUser(id);
            });
        } catch (TransactionException e) {
            throw new CreateUserException();
        }
    }

    @Override
    public User getUser(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_USER, new UserMapper(), id);
        } catch (DataAccessException e) {
            throw new GetUserException();
        }
    }
    @Override
    public List<String> getUsers(List<Integer> toList) {
        StringBuilder stringBuilder = new StringBuilder(GET_USER_NAMES);
        stringBuilder.append(" or id=?".repeat(toList.size()-2));
        try {
            return jdbcTemplate.query(stringBuilder.toString(),preparedStatement -> {
                for (int i = 0; i < toList.size(); i++) {
                    preparedStatement.setInt(i+1,toList.get(i));
                }
            },(rs,map) -> rs.getString("name"));
        } catch (DataAccessException e) {
            throw new GetUsersException();
        }
    }

    @Override
    public void deleteUserWithName(int id) {
        try {

            jdbcTemplate.update(DELETE_USER_WITH_NAME,id);
        } catch (DataAccessException e) {
            throw new DeleteUserException();
        }
    }

    @Override
    public User getUserWithName(String name) {
        try {
            return jdbcTemplate.queryForObject(GET_USER_BY_NAME, new UserMapper(), name);
        } catch (DataAccessException e) {
            throw new GetUserWithNameException();
        }
    }

    @Override
    public List<User> listUsers() {
        try {
            return jdbcTemplate.query(LIST_USERS, new UserMapper());
        } catch (DataAccessException e) {
            throw new ListUsersException();
        }
    }

    @Override
    public boolean existsByUsername(String name) {
        try {
            jdbcTemplate.queryForObject(GET_USER_BY_NAME, new UserMapper(), name);
        }catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            jdbcTemplate.queryForObject(GET_USER_BY_EMAIL, new UserMapper(), email);
        }catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }



    static class Queries {
        public static final String INSERT_USER =
                "INSERT INTO user (name, password, email) " +
                        "VALUES (?, ?, ?)";
        public static final String GET_USER =
                "SELECT * FROM user WHERE id = ?";

        public static final String DELETE_USER_WITH_NAME =
                "delete FROM user WHERE id = ?";

        public static final String GET_USER_NAMES =
                "SELECT name FROM user WHERE id = ?";

        public static final String GET_USER_BY_NAME =
                "SELECT * FROM user WHERE name = ?";

        public static final String GET_USER_BY_EMAIL =
                "SELECT * FROM user WHERE email = ?";

        public static final String LIST_USERS =
                "SELECT * FROM user";
    }
}
