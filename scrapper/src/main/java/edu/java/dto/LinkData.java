package edu.java.dto;

import java.net.URI;
import java.time.OffsetDateTime;

public record LinkData(OffsetDateTime updateTime, URI url) {
}
