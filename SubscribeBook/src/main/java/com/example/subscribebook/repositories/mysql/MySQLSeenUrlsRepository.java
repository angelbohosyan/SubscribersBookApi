package com.example.subscribebook.repositories.mysql;

import com.example.subscribebook.exceptions.SeenUrlRepositoryExceptions.CreateSeenUrlException;
import com.example.subscribebook.mappers.SeenUrlMapper;
import com.example.subscribebook.repositories.SeenUrlsRepository;
import com.example.subscribebook.repositories.UrlUrlResultsRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.Statement;

import static com.example.subscribebook.repositories.mysql.MySQLSeenUrlsRepository.Queries.CREATE_SEEN_URL;
import static com.example.subscribebook.repositories.mysql.MySQLSeenUrlsRepository.Queries.GET_SEEN_URL_BY_ID_URL_AND_PARENT_URL;

public class MySQLSeenUrlsRepository implements SeenUrlsRepository {

    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbcTemplate;

    public MySQLSeenUrlsRepository(JdbcTemplate jdbcTemplate,TransactionTemplate txTemplate) {
        this.txTemplate = txTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createSeenUrl(Integer user_id, String url, String parent_url) {
        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        CREATE_SEEN_URL, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, user_id);
                ps.setString(2, url);
                ps.setString(3, parent_url);
                return ps;
            });
        } catch (DataAccessException e) {
            throw new CreateSeenUrlException();
        }
    }

    @Override
    public boolean getSeenUrlByIdUrlAndParentUrl(Integer user_id, String url, String parent_url) {
        try {
            jdbcTemplate.queryForObject(GET_SEEN_URL_BY_ID_URL_AND_PARENT_URL, new SeenUrlMapper(), user_id, url, parent_url);
        }catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    static class Queries {
        public static final String CREATE_SEEN_URL =
                "INSERT INTO seen_urls (user_id, url,parent_url) " +
                        "VALUES (?, ?,?)";

        public static final String DELETE_URL =
                "DELETE from url where user_id=? and url=?";

        public static final String GET_SEEN_URL_BY_ID_URL_AND_PARENT_URL =
                "select * from seen_urls where user_id=? and url=? and parent_url=?";

        public static final String GET_URL =
                "Select * from url where id=?";

        public static final String GET_URL_BY_USER_ID_AND_URL =
                "Select * from url where id=? and url=?";

        public static final String GET_ALL_URL =
                "Select * from url";

        public static final String GET_ALL_URL_BY_USER_ID =
                "Select url from url where user_id=?";
    }

}
