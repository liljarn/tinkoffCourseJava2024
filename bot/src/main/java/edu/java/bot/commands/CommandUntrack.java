package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.InlineKeyboardBuilder;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CommandUntrack implements Command {
    private static final String GITHUB_LINK = "https://github.com/liljarn/tinkoffCourseJava2024";
    private static final String STACK_LINK =
        "https://stackoverflow.com/questions/53579112/inject-list-of-all-beans-with-a-certain-interface";

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Showing a list of tracked websites. Than user choose websites that will be untracked.";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        return new SendMessage(chatId, "Выбери страницу, которую хочешь перестать отслеживать.")
            .replyMarkup(InlineKeyboardBuilder.createInlineKeyboard(List.of(GITHUB_LINK, STACK_LINK)));
    }
}
