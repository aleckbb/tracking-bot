package edu.java.scrapper.repos.mappers;

import edu.java.scrapper.dtoClasses.jdbc.DTOChat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.RowMapper;

public class ChatMapper implements RowMapper<DTOChat> {
    @Override
    public DTOChat mapRow(ResultSet rs, int rowNum) {
        try {
            return new DTOChat(
                rs.getLong("chat_id"),
                rs.getString("name"),
                rs.getTimestamp("created_at").toLocalDateTime().atOffset(ZoneOffset.UTC)
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
