package com.example.subscribebook.repositories.mysql;

import com.example.subscribebook.repositories.ProfilePictureRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.example.subscribebook.repositories.mysql.MySQLProfilePictureRepository.Queries.*;

public class MySQLProfilePictureRepository implements ProfilePictureRepository {


    private final JdbcTemplate jdbcTemplate;

    public MySQLProfilePictureRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createProfilePicture(int user_id, String path) {
        jdbcTemplate.update(CREATE_PROFILE_PICTURE,user_id,path);
    }

    @Override
    public void deleteProfilePicture(int user_id) {
        jdbcTemplate.update(DELETE_PROFILE_PICTURE,user_id);
    }

    @Override
    public String getPath(int user_id) {
        return jdbcTemplate.queryForObject(GET_PROFILE_PICTURE,(rs, rowNum) -> rs.getString("path"),user_id);
    }

    static class Queries {
        public static final String CREATE_PROFILE_PICTURE =
                "INSERT INTO profile_picture (user_id, path) " +
                        "VALUES (?, ?)";

        public static final String GET_PROFILE_PICTURE =
                "SELECT path FROM profile_picture" +
                        " where user_id=?";

        public static final String DELETE_PROFILE_PICTURE =
                "delete FROM profile_picture" +
                        " where user_id=?";

    }

}
