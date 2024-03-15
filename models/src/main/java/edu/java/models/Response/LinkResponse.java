package edu.java.models.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LinkResponse(@JsonProperty("id") long id,
                            @JsonProperty("url") String url) {
}
