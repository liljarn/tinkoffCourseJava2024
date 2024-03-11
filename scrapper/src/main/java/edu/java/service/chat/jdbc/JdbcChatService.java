package edu.java.service.chat.jdbc;

import edu.java.repository.chat.ChatRepository;
import edu.java.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class JdbcChatService implements ChatService {
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public void registerChat(Long chatId) {
        log.info("Chat was registered successfully!");
        chatRepository.add(chatId);
    }

    @Override
    @Transactional
    public void deleteChat(Long chatId) {
        log.info("Chat was deleted successfully!");
        chatRepository.remove(chatId);
    }
}
