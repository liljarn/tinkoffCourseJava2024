package edu.java.service.chat;

import org.springframework.stereotype.Service;

@Service
public interface ChatService {
    void registerChat(Long chatId);

    void deleteChat(Long chatId);
}
