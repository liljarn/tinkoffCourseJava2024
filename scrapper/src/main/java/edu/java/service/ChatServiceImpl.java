package edu.java.service;

import edu.java.exceptions.ChatBadRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ChatServiceImpl implements ChatService {
    @Override
    public void registerChat(Long chatId) {
        log.info("Chat was registered successfully!");
    }

    @Override
    public void deleteChat(Long chatId) {
        log.info("Chat was deleted successfully!");
    }
}
