package com.example.subscribebook.repositories.mysql;

import com.example.subscribebook.exceptions.FriendRequestRepositoryExceptions.DeletingRequestException;
import com.example.subscribebook.exceptions.FriendsRepositoryExceptions.CreateFriendsException;
import com.example.subscribebook.exceptions.FriendsRepositoryExceptions.DeleteFriendsException;
import com.example.subscribebook.exceptions.FriendsRepositoryExceptions.SelectFriendsException;
import com.example.subscribebook.repositories.FriendsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static com.example.subscribebook.repositories.mysql.MySQLFriendRequestRepository.Queries.DELETE_FRIEND_REQUEST;
import static com.example.subscribebook.repositories.mysql.MySQLFriendsRepository.Queries.*;

public class MySQLFriendsRepository implements FriendsRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public MySQLFriendsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createFriends(int user_id_1, int user_id_2) {
        try {
            jdbcTemplate.update(INSERT_FRIENDS,user_id_1,user_id_2);
        } catch (DataAccessException e) {
            throw new CreateFriendsException();
        }
    }

    @Override
    public List<Integer> getFriends(int user_id) {
        try {
            return jdbcTemplate.query(GET_FRIENDS,(rs, rowNum) -> rs.getInt("user_id_1"),user_id,user_id);
        } catch (DataAccessException e) {
            throw new SelectFriendsException();
        }
    }

    @Override
    public boolean isFriends(int user_id_1, int user_id_2) {
        try {
            return jdbcTemplate.query(GET_FRIEND,(rs, rowNum) -> rs.getInt("user_id_1"),user_id_1,user_id_2,user_id_2,user_id_1).size()>0;
        } catch (DataAccessException e) {
            throw new SelectFriendsException();
        }
    }

    @Override
    public void deleteFriends(int user_id_1, int user_id_2) {
        try {
            jdbcTemplate.update(DELETE_FRIENDS,user_id_1,user_id_2,user_id_2,user_id_1);
        } catch (DataAccessException e) {
            throw new DeleteFriendsException();
        }
    }

    static class Queries {
        public static final String INSERT_FRIENDS =
                "INSERT INTO friends (user_id_1, user_id_2) " +
                        "VALUES (?, ?)";

        public static final String DELETE_FRIENDS =
                "delete from friends where (user_id_1=? and user_id_2=?) OR (user_id_1=? and user_id_2=?)";

        public static final String GET_FRIEND =
                "select user_id_1 from friends where (user_id_1=? and user_id_2=?) OR (user_id_1=? and user_id_2=?)";

        public static final String GET_FRIENDS =
                "select user_id_1 from friends \n" +
                        "where user_id_2 = ?\n" +
                        "union\n" +
                        "select user_id_2 from friends \n" +
                        "where user_id_1 = ?";
    }

}
