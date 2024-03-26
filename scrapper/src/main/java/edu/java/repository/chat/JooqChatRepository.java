package edu.java.repository.chat;

import edu.java.repository.jooq.tables.Chat;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JooqChatRepository implements ChatRepository {
    private final DSLContext context;

    @Override
    public void add(Long chatId) {
        context.insertInto(Chat.CHAT, Chat.CHAT.CHAT_ID).values(chatId).execute();
    }

    @Override
    public void remove(Long chatId) {
        context.deleteFrom(Chat.CHAT).where(Chat.CHAT.CHAT_ID.eq(chatId)).execute();
    }

    @Override
    public boolean isInTable(Long chatId) {
        return context.selectCount().from(Chat.CHAT).where(Chat.CHAT.CHAT_ID.eq(chatId)).fetchOne().value1() != 0;
    }
}
