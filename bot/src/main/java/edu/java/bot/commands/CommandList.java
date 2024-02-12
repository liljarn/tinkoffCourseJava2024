package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.keyboard.InlineKeyboardBuilder;
import edu.java.bot.model.Link;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CommandList implements Command {
    private static final String GITHUB_LINK = "https://github.com/liljarn/tinkoffCourseJava2024";
    private static final String STACK_LINK =
        "https://stackoverflow.com/questions/53579112/inject-list-of-all-beans-with-a-certain-interface";

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "выводит список отслеживаемых ресурсов.";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        return new SendMessage(chatId, "*Вот список отслеживаемых страниц:*\n")
            .replyMarkup(InlineKeyboardBuilder.createUrlKeyboard(List.of(
                new Link(new UUID(1, 2), GITHUB_LINK),
                new Link(new UUID(2, 3), STACK_LINK)
            )));
    }
}
