package edu.java.dtoClasses.jdbc;

import java.time.OffsetDateTime;

public record DTOChat(
    Long chatId,
    String name,
    OffsetDateTime createdAt
) {
}
