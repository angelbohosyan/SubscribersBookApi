package com.example.subscribebook.repositories.mysql;

import com.example.subscribebook.exceptions.SubscribeRepositoryException.CreateSubscriptionException;
import com.example.subscribebook.exceptions.SubscribeRepositoryException.DeleteSubscriptionException;
import com.example.subscribebook.exceptions.SubscribeRepositoryException.GetSubscriptionsException;
import com.example.subscribebook.repositories.SubscribeRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static com.example.subscribebook.repositories.mysql.MySQLSubscribeRepository.Queries.*;

public class MySQLSubscribeRepository implements SubscribeRepository {

    private final JdbcTemplate jdbcTemplate;

    public MySQLSubscribeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createSubscription(int from, int to) {
        try {
            jdbcTemplate.update(INSERT_SUBSCRIPTION,from,to);
        } catch (DataAccessException e) {
            throw new CreateSubscriptionException();
        }
    }

    @Override
    public List<Integer> getSubscriptions(Integer id) {
        try {
            return jdbcTemplate.query(GET_SUBSCRIPTIONS, (rs, map) -> rs.getInt("user_id_to"), id);
        } catch (DataAccessException e) {
            throw new GetSubscriptionsException();
        }
    }

    @Override
    public void deleteSubscription(Integer idFrom, Integer idTo) {
        try {
            jdbcTemplate.update(DELETE_SUBSCRIPTIONS,idFrom,idTo);
        } catch (DataAccessException e) {
            throw new DeleteSubscriptionException();
        }
    }

    static class Queries {
        public static final String INSERT_SUBSCRIPTION =
                "INSERT INTO subscriptions (user_id_from, user_id_to) " +
                        "VALUES (?, ?)";

        public static final String GET_SUBSCRIPTIONS =
                "Select user_id_to From subscriptions where user_id_from=?";

        public static final String DELETE_SUBSCRIPTIONS =
                "delete from subscriptions where user_id_from=? and user_id_to=?";
    }
}
