package edu.java.models.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiErrorResponse(@JsonProperty("description") String description,
                                @JsonProperty("code") String code,
                                @JsonProperty("exceptionName") String exceptionName,
                                @JsonProperty("exceptionMessage") String exceptionMessage,
                                @JsonProperty("stacktrace") String[] stacktrace) {
}
