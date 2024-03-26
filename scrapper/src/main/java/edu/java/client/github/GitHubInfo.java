package edu.java.client.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubInfo(
    String name,
    @JsonProperty("pushed_at")
    OffsetDateTime update
) {
}
