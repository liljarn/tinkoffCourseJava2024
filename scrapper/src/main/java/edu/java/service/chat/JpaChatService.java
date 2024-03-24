package edu.java.service.chat;

import edu.java.exceptions.ChatAlreadyRegisteredException;
import edu.java.exceptions.ChatNotFoundException;
import edu.java.repository.jpa.chat.JpaChatRepository;
import edu.java.repository.jpa.entity.ChatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;

    @Override
    @Transactional
    public void registerChat(Long chatId) {
        if (chatRepository.existsById(chatId)) {
            throw new ChatAlreadyRegisteredException();
        }
        chatRepository.save(new ChatEntity(chatId));
    }

    @Override
    @Transactional
    public void deleteChat(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new ChatNotFoundException(chatId);
        }
        chatRepository.delete(new ChatEntity(chatId));
    }
}
