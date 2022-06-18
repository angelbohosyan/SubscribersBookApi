package com.example.subscribebook.mappers;

import com.example.subscribebook.models.PeopleNewResults;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FollowedPeopleNewResultsRepository implements RowMapper<PeopleNewResults> {

    @Override
    public PeopleNewResults mapRow(ResultSet rs, int map) throws SQLException {
        PeopleNewResults peopleNewResults = new PeopleNewResults();
        peopleNewResults.setUrl(rs.getString("url"));
        peopleNewResults.setNewResult(rs.getString("parent_url"));
        peopleNewResults.setName(rs.getString("name"));
        return peopleNewResults;
    }
}
