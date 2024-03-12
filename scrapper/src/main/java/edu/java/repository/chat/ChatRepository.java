package edu.java.repository.chat;

public interface ChatRepository {
    void add(Long chatId);

    void remove(Long chatId);

    boolean isInTable(Long chatId);
}
