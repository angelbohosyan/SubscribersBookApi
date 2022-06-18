package com.example.subscribebook.mappers;

import com.example.subscribebook.models.UrlWithUrl;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UrlUrlMapper implements RowMapper<UrlWithUrl> {

    @Override
    public UrlWithUrl mapRow(ResultSet rs, int map) throws SQLException {
        final UrlWithUrl urlWithUrl = new UrlWithUrl();
        urlWithUrl.setUrl(rs.getString("url"));
        urlWithUrl.setResultUrl(rs.getString("url_result"));
        return urlWithUrl;
    }
}