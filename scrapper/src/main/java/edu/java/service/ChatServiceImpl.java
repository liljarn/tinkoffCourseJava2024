package edu.java.service;

import edu.java.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    @Override
    public void registerChat(Long chatId) {
        log.info("Chat was registered successfully!");
        chatRepository.add(chatId);
    }

    @Override
    public void deleteChat(Long chatId) {
        log.info("Chat was deleted successfully!");
        chatRepository.remove(chatId);
    }
}
