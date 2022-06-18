package com.example.subscribebook.mappers;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringUrlMapper implements RowMapper<String> {

    @Override
    public String mapRow(ResultSet rs, int map) throws SQLException {
        return rs.getString("url");
    }
}
