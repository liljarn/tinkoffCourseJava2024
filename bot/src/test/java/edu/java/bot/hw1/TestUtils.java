package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import lombok.experimental.UtilityClass;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

@UtilityClass
public class TestUtils {
    public static Update createMockUpdate(String text, long chatId) {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        when(chat.id()).thenReturn(chatId);
        when(message.text()).thenReturn(text);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);
        return update;
    }

    public static Update createMockCallbackUpdate(long chatId) {
        Update update = Mockito.mock(Update.class);
        CallbackQuery callbackQuery = Mockito.mock(CallbackQuery.class);
        User user = Mockito.mock(User.class);
        Mockito.when(user.id()).thenReturn(chatId);
        Mockito.when(callbackQuery.from()).thenReturn(user);
        Mockito.when(update.callbackQuery()).thenReturn(callbackQuery);
        return update;
    }
}
