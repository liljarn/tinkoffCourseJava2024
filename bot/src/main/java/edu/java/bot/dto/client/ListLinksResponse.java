package edu.java.bot.dto.client;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, Integer size) {
}
