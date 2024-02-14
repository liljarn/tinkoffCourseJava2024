package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import static edu.java.bot.utils.MessageConstants.SUCCESSFUL_DELETE;

public class CallbackProcessor extends AbstractProcessor {
    public SendMessage process(Update update) {
        long chatId = update.callbackQuery().from().id();
        return new SendMessage(chatId, SUCCESSFUL_DELETE);
    }

    @Override
    public SendMessage check(Update update) {
        if (update.callbackQuery() != null) {
            return process(update);
        }
        return checkNext(update);
    }
}
