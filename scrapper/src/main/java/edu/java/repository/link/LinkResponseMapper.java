package edu.java.repository.link;

import edu.java.dto.response.LinkResponse;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class LinkResponseMapper implements RowMapper<LinkResponse> {
    @Override
    public LinkResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("link_id");
        URI url = URI.create(rs.getString("url"));
        return new LinkResponse(id, url);
    }
}
