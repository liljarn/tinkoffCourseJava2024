package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.client.ListLinksResponse;
import edu.java.bot.keyboard.InlineKeyboardBuilder;
import edu.java.bot.service.command.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.MessageConstants.LIST_COMMAND;
import static edu.java.bot.utils.MessageConstants.LIST_COMMANDS_TEXT;
import static edu.java.bot.utils.MessageConstants.LIST_DESCRIPTION;
import static edu.java.bot.utils.MessageConstants.LIST_WRONG_TEXT;

@Component
@RequiredArgsConstructor
public class CommandList implements Command {
    private final CommandService commandService;

    @Override
    public String command() {
        return LIST_COMMAND;
    }

    @Override
    public String description() {
        return LIST_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        if (update.message().text().equals(command())) {
            ListLinksResponse links = commandService.getLinks(chatId);
            return new SendMessage(chatId, LIST_COMMANDS_TEXT)
                .replyMarkup(InlineKeyboardBuilder.createUrlKeyboard(links.links()));
        }
        return new SendMessage(chatId, LIST_WRONG_TEXT);
    }
}
