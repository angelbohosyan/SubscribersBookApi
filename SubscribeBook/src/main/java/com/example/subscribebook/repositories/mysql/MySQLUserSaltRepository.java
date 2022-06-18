package com.example.subscribebook.repositories.mysql;

import com.example.subscribebook.exceptions.UserSaltRepositoryExceptions.CreateUserSaltException;
import com.example.subscribebook.exceptions.UserSaltRepositoryExceptions.DeleteUserSaltException;
import com.example.subscribebook.exceptions.UserSaltRepositoryExceptions.GetSaltByUserException;
import com.example.subscribebook.mappers.SaltMapper;
import com.example.subscribebook.repositories.UserSaltRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.Statement;

import static com.example.subscribebook.repositories.mysql.MySQLUserSaltRepository.Queries.*;


public class MySQLUserSaltRepository implements UserSaltRepository {

    private final JdbcTemplate jdbcTemplate;

    public MySQLUserSaltRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUserSalt(int user_id, String salt) {
        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        INSERT_USER_SALT, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, user_id);
                ps.setString(2, salt);
                return ps;
            });
        } catch (DataAccessException e) {
            throw new CreateUserSaltException();
        }
    }

    @Override
    public String getSaltByUser(int user_id) {
        try {
            return jdbcTemplate.queryForObject(GET_SALT_BY_USER,new SaltMapper(),user_id);
        } catch (DataAccessException e) {
            throw new GetSaltByUserException();
        }
    }

    @Override
    public void deleteUserSalt(int user_id) {
        try {
            jdbcTemplate.update(DELETE_SALT_BY_USER,user_id);
        } catch (DataAccessException e) {
            throw new DeleteUserSaltException();
        }
    }

    static class Queries {
        public static final String INSERT_USER_SALT =
                "INSERT INTO user_salt (user_id, salt) " +
                        "VALUES (?, ?)";

        public static final String GET_SALT_BY_USER =
                "Select salt from user_salt where user_id = ?";

        public static final String DELETE_SALT_BY_USER =
                "delete from user_salt where user_id = ?";

    }
}
