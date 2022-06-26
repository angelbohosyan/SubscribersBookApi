package com.example.subscribebook.repositories.mysql;

import com.example.subscribebook.exceptions.UrlUrlRepositoryExceptions.CreateMultipleUrlUrlResultsException;
import com.example.subscribebook.exceptions.UrlUrlRepositoryExceptions.CreateUrlUrlResultsException;
import com.example.subscribebook.exceptions.UrlUrlRepositoryExceptions.DeleteUrlUrlResultException;
import com.example.subscribebook.exceptions.UrlUrlRepositoryExceptions.GetUrlUrlResultsWithUrlException;
import com.example.subscribebook.mappers.UrlUrlMapper;
import com.example.subscribebook.models.UrlWithUrl;
import com.example.subscribebook.repositories.UrlUrlResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static com.example.subscribebook.repositories.mysql.MySQLUrlUrlResultsRepository.Queries.*;

public class MySQLUrlUrlResultsRepository implements UrlUrlResultsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MySQLUrlUrlResultsRepository(JdbcTemplate jdbcTemplate) {
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
            throw new CreateUrlUrlResultsException();
        }
    }



    @Override
    public void createMultipleUrlUrlResults(List<String> urls, Integer url_id) {

        if(urls.size()==0) return;

        if(urls.size()==1) {
            createUrlUrlResults(urls.get(0),url_id);
            return;
        }

        StringBuilder INSERT_MULTIPLE_URL_URL_RESULTS = new StringBuilder(INSERT_URL_URL_RESULTS);

        INSERT_MULTIPLE_URL_URL_RESULTS.append(", (?,?)".repeat(urls.size()-1));

        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        INSERT_MULTIPLE_URL_URL_RESULTS.toString(), Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < urls.size(); i++) {
                    ps.setInt(i*2+1, url_id);
                    ps.setString(i*2+2, urls.get(i));
                }
                return ps;
            });
        } catch (DataAccessException e) {
            throw new CreateMultipleUrlUrlResultsException();
        }
    }

    @Override
    public List<String> getUrlUrlResultsWithUrlId(Integer id) {
        return jdbcTemplate.query(GET_URL_BY_ID,(rs, rowNum) -> rs.getString("url"),id);
    }

    @Override
    public List<UrlWithUrl> getUrlUrlResultsWithUrl() {
        try {
            return jdbcTemplate.query(JOIN_ON_URL_ID_WITH_URL,new UrlUrlMapper());
        } catch (DataAccessException e) {
            throw new GetUrlUrlResultsWithUrlException();
        }
    }

    @Override
    public void deleteUrlUrlResult(List<Integer> toList) {
        if(toList.size()==0) return;
        StringBuilder stringBuilder = new StringBuilder(DELETE_URL_URL_RESULTS);
        stringBuilder.append(" or url_id=?".repeat(toList.size()-1));
        try {
            jdbcTemplate.update(stringBuilder.toString(),preparedStatement -> {
                for (int i = 0; i < toList.size(); i++) {
                    preparedStatement.setInt(i+1,toList.get(i));
                }
            });
        } catch (DataAccessException e) {
            throw new DeleteUrlUrlResultException();
        }
    }

    static class Queries {
        public static final String INSERT_URL_URL_RESULTS =
                "INSERT INTO url_url_results (url_id, url) " +
                        "VALUES (?, ?)";

        public static final String DELETE_URL_URL_RESULTS =
                "delete from url_url_results where url_id=?";

        public static final String GET_URL_BY_ID =
                "select url from url_url_results where url_id=?";

        public static final String JOIN_ON_URL_ID_WITH_URL =
                "SELECT url.url as url,url_url_results.url as url_result FROM url_url_results\n" +
                        "join url on url.id=url_url_results.url_id;";
    }
}
