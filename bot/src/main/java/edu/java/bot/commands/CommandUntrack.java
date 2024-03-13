package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.client.LinkResponse;
import edu.java.bot.keyboard.InlineKeyboardBuilder;
import edu.java.bot.service.command.CommandService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.MessageConstants.EMPTY_TRACK_LIST;
import static edu.java.bot.utils.MessageConstants.UNTRACK_COMMAND;
import static edu.java.bot.utils.MessageConstants.UNTRACK_DESCRIPTION;
import static edu.java.bot.utils.MessageConstants.UNTRACK_MESSAGE;
import static edu.java.bot.utils.MessageConstants.UNTRACK_WRONG_TEXT;

@Component
@RequiredArgsConstructor
public class CommandUntrack implements Command {
    private final CommandService service;

    @Override
    public String command() {
        return UNTRACK_COMMAND;
    }

    @Override
    public String description() {
        return UNTRACK_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        if (update.message().text().equals(command())) {
            List<LinkResponse> links = service.getLinks(chatId).links();
            if (links.isEmpty()) {
                return new SendMessage(chatId, EMPTY_TRACK_LIST);
            }
            return new SendMessage(chatId, UNTRACK_MESSAGE)
                .replyMarkup(InlineKeyboardBuilder.createCallbackKeyboard(links));
        }
        return new SendMessage(chatId, UNTRACK_WRONG_TEXT);
    }
}
