package com.example.subscribebook.repositories.mysql;

import com.example.subscribebook.exceptions.UrlRepositoryExceptions.*;
import com.example.subscribebook.mappers.StringUrlMapper;
import com.example.subscribebook.mappers.UrlMapper;
import com.example.subscribebook.mappers.FollowedPeopleNewResultsRepository;
import com.example.subscribebook.models.PeopleNewResults;
import com.example.subscribebook.models.UrlDAO;
import com.example.subscribebook.repositories.UrlRepository;
import com.example.subscribebook.repositories.UrlUrlResultsRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.subscribebook.repositories.mysql.MySQLUrlRepository.Queries.*;

public class MySQLUrlRepository implements UrlRepository {

    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbcTemplate;

    private final UrlUrlResultsRepository urlUrlResultsRepository;

    public MySQLUrlRepository(JdbcTemplate jdbcTemplate,TransactionTemplate txTemplate,UrlUrlResultsRepository urlUrlResultsRepository) {
        this.txTemplate = txTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.urlUrlResultsRepository = urlUrlResultsRepository;
    }


    @Override
    public UrlDAO createUrl(String url, Integer user_id) {
        try {
        return txTemplate.execute(status -> {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    INSERT_URL, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, user_id);
            ps.setString(2, url);
            return ps;
        }, keyHolder);
            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return getUrl(id);
        });
        } catch(DataAccessException e) {
            throw new CreateUrlException();
        }
    }

    @Override
    public List<UrlDAO> getUrls() {
        try {
            return jdbcTemplate.query(GET_ALL_URL, new UrlMapper());
        } catch (DataAccessException a) {
            throw new GetUrlsException();
        }
    }

    @Override
    public List<Integer> getUrlsById(int id) {
        try {
            return jdbcTemplate.query(GET_ALL_URL_ID_BY_USER_ID, (rs,map)-> rs.getInt("id"),id);
        } catch (DataAccessException a) {
            throw new GetUrlsException();
        }
    }

    @Override
    public List<String> getUrlsByUserId(Integer id) {
        try {
            return jdbcTemplate.query(GET_ALL_URL_BY_USER_ID, new StringUrlMapper(), id);
        } catch (DataAccessException e) {
            throw new GetUrlsByUserIdException();
        }
    }

    @Override
    public boolean getUrlsByUserIdAndUrl(Integer urlId,Integer userId) {
        try {
            return jdbcTemplate.query(GET_URL_BY_ID_AND_USER_ID, new StringUrlMapper(), urlId,userId).size()>0;
        } catch (DataAccessException e) {
            throw new GetUrlsByUserIdException();
        }
    }

    @Override
    public List<Integer> getUrlsByUsersId(List<Integer> followedPeopleList) {
        if(followedPeopleList.size()==0) return new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder(GET_ALL_ID_BY_USER_ID);
        stringBuilder.append(" or user_id=?".repeat(followedPeopleList.size()-1));
        try {
            return jdbcTemplate.query(stringBuilder.toString(),preparedStatement -> {
                for (int i = 0; i < followedPeopleList.size(); i++) {
                    preparedStatement.setInt(i+1,followedPeopleList.get(i));
                }
            },(rs,map) -> rs.getInt("id"));
        } catch (DataAccessException e) {
            throw new GetUrlsByUsersIdException();
        }
    }

    @Override
    public List<PeopleNewResults> getUsersIdById(List<Integer> publicUrlsOfFollowedPeople) {
        if(publicUrlsOfFollowedPeople.size()==0) return new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder(GET_ALL_USER_ID_BY_ID);
        stringBuilder.append(" or id=?".repeat(publicUrlsOfFollowedPeople.size()-1));
        try {
            return jdbcTemplate.query(stringBuilder.toString(),preparedStatement -> {
                for (int i = 0; i < publicUrlsOfFollowedPeople.size(); i++) {
                    preparedStatement.setInt(i+1,publicUrlsOfFollowedPeople.get(i));
                }
            },new FollowedPeopleNewResultsRepository());
        } catch (DataAccessException e) {
            throw new GetUsersIdByIdException();
        }
    }

    @Override
    public Integer getUserIdById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(GET_USER_ID_BY_ID,(rs,map) -> rs.getInt("user_id"),id);
        } catch (DataAccessException e) {
            throw new GetUserIdByIdException();
        }
    }

    @Override
    public void deleteUrlById(Integer id) {
        jdbcTemplate.update(DELETE_URL_BY_ID,id);
    }

    @Transactional
    @Override
    public void deleteUrlsByUserIdAndUrlName(Integer id, String resultUrl) {
        try {
            List<UrlDAO> list = jdbcTemplate.query(GET_URL_BY_ID_AND_URL,new UrlMapper(),id,resultUrl);
            urlUrlResultsRepository.deleteUrlUrlResult(list.stream().map(UrlDAO::getId).toList());
            jdbcTemplate.update(DELETE_URL_BY_USER_ID_AND_USER_ID,id,resultUrl);
        } catch (DataAccessException e) {
            throw new DeleteUrlsByUserIdAndUrlNameException();
        }
    }

    @Override
    public Integer getIdByUserIdAndUrl(Integer id, String resultUrl) {
        try {
            return jdbcTemplate.queryForObject(GET_URL_BY_ID_AND_URL, new UrlMapper(), id,resultUrl).getId();
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new GetIdByUserIdAndUrlException();
        }
    }

    @Override
    public boolean exist(String resultUrl, Integer id) {
        try {
            jdbcTemplate.queryForObject(GET_URL_BY_ID_AND_URL, new UrlMapper(),  id,resultUrl);
        }catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    private UrlDAO getUrl(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_URL, new UrlMapper(), id);
        } catch (DataAccessException e) {
            throw new GetUrlException();
        }
    }

    static class Queries {
        public static final String INSERT_URL =
                "INSERT INTO url (user_id, url) " +
                        "VALUES (?, ?)";

        public static final String DELETE_URL_BY_USER_ID_AND_USER_ID =
                "DELETE from url where user_id=? and url=?";

        public static final String DELETE_URL_BY_ID =
                "DELETE from url where id=?";

        public static final String GET_URL_BY_ID_AND_URL =
                "select * from url " +
                        "where user_id=? " +
                        "and url=?";

        public static final String GET_URL =
                "Select * from url where id=?";

        public static final String GET_URL_BY_USER_ID_AND_URL =
                "Select * from url where id=? and url=?";

        public static final String GET_ALL_URL =
                "Select * from url";
        public static final String GET_ALL_URL_BY_USER_ID =
                "Select url from url where user_id=?";

        public static final String GET_ALL_URL_ID_BY_USER_ID =
                "Select id from url where user_id=?";

        public static final String GET_ALL_ID_BY_USER_ID =
                "Select id from url where user_id=?";

        public static final String GET_USER_ID_BY_ID =
                "Select user_id from url where id=?";

        public static final String GET_URL_BY_ID_AND_USER_ID =
                "Select * from url where id=? and user_id=?";

        public static final String GET_ALL_USER_ID_BY_ID =
                "Select nuur.url,us.name,ur.url as parent_url \n" +
                        "from url ur\n" +
                        "join user us on us.id = ur.user_id\n" +
                        "join new_url_url_results nuur on nuur.parent_url = ur.url\n" +
                        "where ur.id=?";

    }
}
