package edu.java.dto;

import java.util.Set;

public record ChatLinkResponse(Long linkId, Set<Long> tgChatIds) {
}
