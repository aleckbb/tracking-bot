package edu.java.models.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LinkUpdate(@JsonProperty("id") int id,
                         @JsonProperty("url") String url,
                         @JsonProperty("description") String description,
                         @JsonProperty("tgChatIds") int[] tgChatIds) {

}
