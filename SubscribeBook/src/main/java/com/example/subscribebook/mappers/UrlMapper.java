package com.example.subscribebook.mappers;

import com.example.subscribebook.models.UrlDAO;
import com.example.subscribebook.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UrlMapper implements RowMapper<UrlDAO> {

    @Override
    public UrlDAO mapRow(ResultSet rs, int map) throws SQLException {
        final UrlDAO user = new UrlDAO();
        user.setId(rs.getInt("id"));
        user.setUser_id(rs.getInt("user_id"));
        user.setUrl(rs.getString("url"));
        return user;
    }
}