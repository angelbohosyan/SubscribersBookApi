package com.example.subscribebook.repositories.mysql;

import com.example.subscribebook.repositories.UrlErrorRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.example.subscribebook.repositories.mysql.MySQLUrlErrorRepository.Queries.INSERT_URL_ERROR;

public class MySQLUrlErrorRepository implements UrlErrorRepository {

    private final JdbcTemplate jdbcTemplate;

    public MySQLUrlErrorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUrlError(String url) {
        jdbcTemplate.update(INSERT_URL_ERROR,url);
    }

    static class Queries {
        public static final String INSERT_URL_ERROR =
                "INSERT INTO url_error (url) " +
                        "VALUES (?)";


    }
}
