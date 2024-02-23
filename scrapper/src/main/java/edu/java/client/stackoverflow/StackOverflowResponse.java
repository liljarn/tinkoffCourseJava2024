package edu.java.client.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowResponse(StackOverflowInfo[] items) {
    public record StackOverflowInfo(
        String title,
        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate
    ) {
    }
}
