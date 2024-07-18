package edu.java.scrapper.dtoClasses.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record Repository(@JsonProperty("name") String repoName,
                         @JsonProperty("pushed_at") OffsetDateTime pushedTime) {

}
