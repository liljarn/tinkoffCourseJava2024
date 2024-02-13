package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.keyboard.InlineKeyboardBuilder;
import edu.java.bot.model.Link;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.MessageConsts.GITHUB_LINK;
import static edu.java.bot.utils.MessageConsts.STACK_LINK;
import static edu.java.bot.utils.MessageConsts.UNTRACK_COMMAND;
import static edu.java.bot.utils.MessageConsts.UNTRACK_DESCRIPTION;
import static edu.java.bot.utils.MessageConsts.UNTRACK_MESSAGE;
import static edu.java.bot.utils.MessageConsts.UNTRACK_WRONG_TEXT;

@Component
public class CommandUntrack implements Command {
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
            return new SendMessage(chatId, UNTRACK_MESSAGE)
                .replyMarkup(InlineKeyboardBuilder.createCallbackKeyboard(List.of(
                    new Link(new UUID(1, 2), GITHUB_LINK),
                    new Link(new UUID(2, 2), STACK_LINK)
                )));
        }
        return new SendMessage(chatId, UNTRACK_WRONG_TEXT);
    }
}
