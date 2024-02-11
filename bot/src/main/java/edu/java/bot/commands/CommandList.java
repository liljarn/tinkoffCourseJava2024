package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.InlineKeyboardBuilder;
import java.util.List;
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
        return new SendMessage(chatId, "Вот список отслеживаемых страниц:\n")
            .replyMarkup(InlineKeyboardBuilder.createInlineKeyboard(List.of(GITHUB_LINK, STACK_LINK)));
    }
}
