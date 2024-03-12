package edu.java.repository.link.mapper;

import edu.java.dto.LinkData;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.RowMapper;

public class LinkDataMapper implements RowMapper<LinkData> {
    @Override
    public LinkData mapRow(ResultSet rs, int rowNum) throws SQLException {
        OffsetDateTime updateTime = rs.getTimestamp("last_update_time").toLocalDateTime().atOffset(ZoneOffset.UTC);
        URI url = URI.create(rs.getString("url"));
        return new LinkData(updateTime, url);
    }
}
