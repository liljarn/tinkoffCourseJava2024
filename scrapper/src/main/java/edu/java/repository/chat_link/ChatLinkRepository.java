package edu.java.repository.chat_link;

import edu.java.dto.ChatLinkResponse;
import java.util.List;

public interface ChatLinkRepository {
    List<ChatLinkResponse> findAll();

    void add(Long chatId, Long linkId);

    boolean delete(Long chatId, Long linkId);
}
