package com.example.subscribebook.repositories.mysql;

import com.example.subscribebook.exceptions.FriendRequestRepositoryExceptions.DeletingRequestException;
import com.example.subscribebook.exceptions.FriendRequestRepositoryExceptions.FriendRequestAlreadyExistsException;
import com.example.subscribebook.repositories.FriendRequestRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.example.subscribebook.repositories.mysql.MySQLFriendRequestRepository.Queries.*;

public class MySQLFriendRequestRepository implements FriendRequestRepository {

    private final JdbcTemplate jdbcTemplate;

    public MySQLFriendRequestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createFriendRequest(int from, int to) {
        try {
            jdbcTemplate.update(CREATE_FRIEND_REQUEST, from, to);
        } catch (DataAccessException e) {
            throw new FriendRequestAlreadyExistsException();
        }
    }

    @Override
    public boolean getFriendRequestFromTo(int from, int to) {
        try {
            jdbcTemplate.queryForObject(GET_FRIEND_REQUEST, (rs,map) -> rs.getInt("user_id_sent"),  from,to);
        }catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public void deleteFriendsRequest(int from, int to) {
        try {
            jdbcTemplate.update(DELETE_FRIEND_REQUEST,from,to);
        } catch (DataAccessException e) {
            throw new DeletingRequestException();
        }
    }

    static class Queries {
        public static final String CREATE_FRIEND_REQUEST =
                "INSERT INTO friend_requests (user_id_sent, user_id_to) " +
                        "VALUES (?, ?)";

        public static final String DELETE_FRIEND_REQUEST =
                "delete from friend_requests where user_id_sent=? and user_id_to=?";

        public static final String GET_FRIEND_REQUEST =
                "select user_id_sent from friend_requests where user_id_sent=? and user_id_to=?";

    }
}
