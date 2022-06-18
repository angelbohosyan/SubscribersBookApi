package com.example.subscribebook.mappers;

import com.example.subscribebook.models.SeenUrl;
import com.example.subscribebook.models.UrlDAO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeenUrlMapper implements RowMapper<SeenUrl> {

    @Override
    public SeenUrl mapRow(ResultSet rs, int map) throws SQLException {
        final SeenUrl seenUrl = new SeenUrl();
        seenUrl.setUserId(rs.getInt("user_id"));
        seenUrl.setUrl(rs.getString("url"));
        seenUrl.setParentUrl(rs.getString("parent_url"));
        return seenUrl;
    }
}