package com.example.subscribebook.repositories.mysql;

import com.example.subscribebook.exceptions.UrlScopeRepositoryExceptions.CreateUrlScopeException;
import com.example.subscribebook.exceptions.UrlScopeRepositoryExceptions.DeleteUrlScopeException;
import com.example.subscribebook.exceptions.UrlScopeRepositoryExceptions.GetUrlScopeFriendException;
import com.example.subscribebook.exceptions.UrlScopeRepositoryExceptions.GetUrlScopePublicException;
import com.example.subscribebook.repositories.UrlScopeRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.example.subscribebook.repositories.mysql.MySQLUrlScopeRepository.Queries.*;
import static com.example.subscribebook.repositories.mysql.MySQLUrlUrlResultsRepository.Queries.DELETE_URL_URL_RESULTS;

public class MySQLUrlScopeRepository implements UrlScopeRepository {

    private JdbcTemplate jdbcTemplate;

    public MySQLUrlScopeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUrlScope(int urlId, String scope) {
        try {
            jdbcTemplate.update(INSERT_URL_SCOPE,urlId,scope);
        } catch (DataAccessException e) {
            throw new CreateUrlScopeException();
        }
    }

    @Override
    public void deleteUrlScope(int urlId) {
        try {
            jdbcTemplate.update(DELETE_URL_SCOPE,urlId);
        } catch (DataAccessException e) {
            throw new DeleteUrlScopeException();
        }
    }

    @Override
    public List<Integer> getUrlScopePublic(List<Integer> urlId) {
        StringBuilder stringBuilder = new StringBuilder(GET_URL_SCOPE_PUBLIC);
        if(urlId.size()==0) return new ArrayList<>();
        if(urlId.size()==1) stringBuilder.append("url_id=?");
        else {
            stringBuilder.append("(url_id=?");
            stringBuilder.append(" or url_id=?".repeat(urlId.size()-2));
            stringBuilder.append(" or url_id=?)");
        }
        try {
            return jdbcTemplate.query(stringBuilder.toString(),preparedStatement -> {
                for (int i = 0; i < urlId.size(); i++) {
                    preparedStatement.setInt(i+1,urlId.get(i));
                }
            },(rs,map) -> rs.getInt("url_id"));
        } catch (DataAccessException e) {
            throw new GetUrlScopePublicException();
        }
    }

    @Override
    public List<Integer> getUrlScopeFriend(List<Integer> urlId) {
        StringBuilder stringBuilder = new StringBuilder(GET_URL_SCOPE_FRIEND);
        if(urlId.size()==0) return new ArrayList<>();
        if(urlId.size()==1) stringBuilder.append("url_id=?");
        else {
            stringBuilder.append("(url_id=?");
            stringBuilder.append(" or url_id=?".repeat(urlId.size()-2));
            stringBuilder.append(" or url_id=?)");
        }
        try {
            return jdbcTemplate.query(stringBuilder.toString(),preparedStatement -> {
                for (int i = 0; i < urlId.size(); i++) {
                    preparedStatement.setInt(i+1,urlId.get(i));
                }
            },(rs,map) -> rs.getInt("url_id"));
        } catch (DataAccessException e) {
            throw new GetUrlScopeFriendException();
        }
    }

    static class Queries {
        public static final String INSERT_URL_SCOPE =
                "INSERT INTO url_scope (url_id, scope) " +
                        "VALUES (?, ?)";

        public static final String DELETE_URL_SCOPE =
                "delete from url_scope where url_id=?";

        public static final String GET_URL_SCOPE_PUBLIC =
                "Select url_id From url_scope where scope='public' and ";

        public static final String GET_URL_SCOPE_FRIEND =
                "Select url_id From url_scope where (scope='friend' or scope='public') and ";
    }

}
