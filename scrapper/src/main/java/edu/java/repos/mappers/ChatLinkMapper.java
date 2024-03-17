package edu.java.repos.mappers;

import edu.java.dtoClasses.jdbc.DTOSub;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ChatLinkMapper implements RowMapper<DTOSub> {
    @Override
    public DTOSub mapRow(ResultSet rs, int rowNum) {
        try {
            return new DTOSub(
                rs.getLong("chat_id"),
                rs.getLong("link_id")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
