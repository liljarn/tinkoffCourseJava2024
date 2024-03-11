package edu.java.dto;

import java.util.List;

public record ChatLinkResponse(Long linkId, List<Long> tgChatIds) {
}
