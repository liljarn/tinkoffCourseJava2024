package edu.java.bot.dto.client;

import jakarta.validation.constraints.NotNull;

public record RemoveLinkRequest(@NotNull Long id) {
}
