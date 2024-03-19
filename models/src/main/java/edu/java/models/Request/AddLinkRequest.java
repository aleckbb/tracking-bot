package edu.java.models.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddLinkRequest(@JsonProperty("link") String link) {

}
