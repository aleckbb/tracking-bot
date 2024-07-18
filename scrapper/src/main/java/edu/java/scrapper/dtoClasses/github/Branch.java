package edu.java.scrapper.dtoClasses.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Branch(
    @JsonProperty("name") String name,
    @JsonProperty("commit") Commit commit
) {
    public record Commit(
        @JsonProperty("sha") String sha
    ) {
    }
}
