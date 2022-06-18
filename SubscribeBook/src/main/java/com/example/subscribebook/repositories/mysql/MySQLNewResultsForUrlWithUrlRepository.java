package com.example.subscribebook.repositories.mysql;

import com.example.subscribebook.exceptions.NewResultsExceptions.CreateNewMultipleUrlUrlResultsException;
import com.example.subscribebook.exceptions.NewResultsExceptions.CreateNewUrlUrlResultsException;
import com.example.subscribebook.exceptions.NewResultsExceptions.GetAllNewUrlUrlResultsWithUrlException;
import com.example.subscribebook.exceptions.NewResultsExceptions.GetNewUrlUrlResultsWithUrlException;
import com.example.subscribebook.mappers.UrlUrlMapper;
import com.example.subscribebook.models.UrlWithUrl;
import com.example.subscribebook.repositories.NewResultsForUrlWithUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.subscribebook.repositories.mysql.MySQLNewResultsForUrlWithUrlRepository.Queries.*;

public class MySQLNewResultsForUrlWithUrlRepository implements NewResultsForUrlWithUrlRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MySQLNewResultsForUrlWithUrlRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUrlUrlResults(String url, Integer url_id) {
        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        INSERT_URL_URL_RESULTS, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, url_id);
                ps.setString(2, url);
                return ps;
            });
        } catch (DataAccessException e) {
            throw new CreateNewUrlUrlResultsException();
        }
    }

    @Override
    public void createMultipleUrlUrlResults(HashMap<String, HashSet<String>> map){

        if(map.size()==0) return;

        StringBuilder INSERT_MULTIPLE_URL_URL_RESULTS = new StringBuilder(INSERT_URL_URL_RESULTS);

        int mapSize = map.values().stream().mapToInt(HashSet::size).sum();

        INSERT_MULTIPLE_URL_URL_RESULTS.append(", (?,?)".repeat(mapSize-1));

        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        INSERT_MULTIPLE_URL_URL_RESULTS.toString(), Statement.RETURN_GENERATED_KEYS);
                AtomicInteger i = new AtomicInteger();
                map.forEach((k, v) -> v.forEach((p) -> {
                    try {
                        ps.setString(i.get() * 2 + 1, k);
                        ps.setString(i.get() * 2 + 2, p);
                        i.getAndIncrement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }));
                return ps;
            });
        } catch (DataAccessException e) {
            throw new CreateNewMultipleUrlUrlResultsException();
        }
    }

    @Override
    public List<UrlWithUrl> getUrlUrlResultsWithUrl(List<String> urls) {
        if(urls.size()==0) return new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder(GET_URL_WITH_URL);
        stringBuilder.append(" or parent_url=?".repeat(urls.size()-1));
        try {
            return jdbcTemplate.query(stringBuilder.toString(), preparedStatement -> {
                for (int i = 0; i < urls.size(); i++) {
                    preparedStatement.setString(i + 1, urls.get(i));
                }
            }, new UrlUrlMapper());
        } catch (DataAccessException e) {
            throw new GetNewUrlUrlResultsWithUrlException();
        }
    }

    @Override
    public List<UrlWithUrl> getAllUrlUrlResultsWithUrl() {
        try {
            return jdbcTemplate.query(GET_ALL_URL_WITH_URL,new UrlUrlMapper());
        }catch (DataAccessException e) {
            throw new GetAllNewUrlUrlResultsWithUrlException();
        }
    }

    @Override
    public boolean exist(UrlWithUrl urlWithUrl) {
        try {
            jdbcTemplate.queryForObject(GET_URL_WITH_URL_BY_URL_AND_PARENT_URL, new UrlUrlMapper(), urlWithUrl.getResultUrl(),urlWithUrl.getUrl());
        }catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    static class Queries {
        public static final String INSERT_URL_URL_RESULTS =
                "INSERT INTO new_url_url_results (parent_url, url) " +
                        "VALUES (?, ?)";

        public static final String GET_URL_WITH_URL =
                "SELECT url as url,parent_url as url_result FROM new_url_url_results" +
                        " where parent_url=?";

        public static final String GET_URL_WITH_URL_BY_URL_AND_PARENT_URL =
                "SELECT url as url,parent_url as url_result FROM new_url_url_results" +
                        " where parent_url=? and url=?";

        public static final String GET_ALL_URL_WITH_URL =
                "SELECT url as url,parent_url as url_result FROM new_url_url_results";
    }
}
