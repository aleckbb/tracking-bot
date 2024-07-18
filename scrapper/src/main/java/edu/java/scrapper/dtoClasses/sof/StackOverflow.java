package edu.java.scrapper.dtoClasses.sof;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflow(@JsonProperty("items") List<Question> items) {
    public record Question(@JsonProperty("is_answered") boolean isAnswered,
                           @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate,
                           @JsonProperty("title") String title) {

    }
}
