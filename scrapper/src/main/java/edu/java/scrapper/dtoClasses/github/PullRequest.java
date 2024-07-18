package edu.java.scrapper.dtoClasses.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record PullRequest(
    @JsonProperty("number") int number,
    @JsonProperty("title") String title,
    @JsonProperty("created_at") OffsetDateTime createdAt
) {
}
