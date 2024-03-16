package edu.java.client.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubEvent(String type, EventInfo payload, @JsonProperty("created_at") OffsetDateTime updateTime) {
    public record EventInfo(String ref, IssueInfo issue, String action) {
        public record IssueInfo(String title){
        }
    }
}
