package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class CallbackProcessor extends AbstractProcessor {
    public SendMessage process(Update update) {
        long chatId = update.callbackQuery().from().id();
        return new SendMessage(chatId, "Ссылка успешно удалена.");
    }

    @Override
    public SendMessage check(Update update) {
        if (update.callbackQuery() != null) {
            return process(update);
        }
        return checkNext(update);
    }
}
