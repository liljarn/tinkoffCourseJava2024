package edu.java.bot.dto.client;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record AddLinkRequest(@NotNull URI link) {
}
