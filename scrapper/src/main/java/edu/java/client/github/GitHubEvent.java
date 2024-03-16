package edu.java.client.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubEvent(EventType type, EventInfo payload, @JsonProperty("created_at") OffsetDateTime updateTime) {
    public enum EventType {
        @JsonProperty("PushEvent")
        PUSH_EVENT("PushEvent"),
        @JsonProperty("IssueEvent")
        ISSUE_EVENT("IssueEvent");

        private final String eventType;

        EventType(String eventType) {
            this.eventType = eventType;
        }

        public String getType() {
            return eventType;
        }
    }

    public record EventInfo(String ref, IssueInfo issue) {
        public record IssueInfo(String issue){
        }
    }
}
