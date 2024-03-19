package edu.java.dtoClasses.sof;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record DTOStackOverflow(@JsonProperty("items") List<QuestionParam> items) {
    public record QuestionParam(@JsonProperty("last_activity_date") OffsetDateTime lastActivityDate,
                                @JsonProperty("creation_date") OffsetDateTime creationDate,
                                @JsonProperty("question_id") String id) {

    }
}
