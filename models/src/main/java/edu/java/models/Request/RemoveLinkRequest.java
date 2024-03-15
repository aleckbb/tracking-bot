package edu.java.models.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RemoveLinkRequest(@JsonProperty("link") String link) {
}
