package edu.java.client.dto;

import java.net.URI;
import java.time.OffsetDateTime;

public record LinkInfo(URI url, String title, OffsetDateTime lastActivityDate) {
}
