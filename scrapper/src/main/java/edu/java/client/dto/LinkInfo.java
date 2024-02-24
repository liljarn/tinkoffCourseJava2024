package edu.java.client.dto;

import java.net.URL;
import java.time.OffsetDateTime;

public record LinkInfo(URL url, String title, OffsetDateTime lastActivityDate) {
}
