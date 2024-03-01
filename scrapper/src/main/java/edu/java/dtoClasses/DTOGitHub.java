package edu.java.dtoClasses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record DTOGitHub(@JsonProperty("name") String repoName,
                        @JsonProperty("created_at") OffsetDateTime createdTime,
                        @JsonProperty("updated_at") OffsetDateTime updatedTime,
                        @JsonProperty("pushed_at") OffsetDateTime pushedTime) {

}
