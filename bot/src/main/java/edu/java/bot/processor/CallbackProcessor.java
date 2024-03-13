package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.client.RemoveLinkRequest;
import edu.java.bot.service.command.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.MessageConstants.SUCCESSFUL_DELETE;

@Component
@RequiredArgsConstructor
@Log4j2
public class CallbackProcessor extends AbstractProcessor {
    private final CommandService service;

    public SendMessage process(Update update) {
        long chatId = update.callbackQuery().from().id();
        RemoveLinkRequest removeLinkRequest =
            new RemoveLinkRequest(Long.valueOf(update.callbackQuery().data()));
        log.info(removeLinkRequest);
        service.removeLink(chatId, removeLinkRequest);
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
