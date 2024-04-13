package edu.java.scrapper.repos.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SofData(
    @JsonProperty boolean isAnswered
) {
}
