package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.keyboard.InlineKeyboardBuilder;
import edu.java.bot.model.Link;
import java.util.List;
import java.util.UUID;
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
        return "не отслеживать определённый ресурс.";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        if (update.message().text().equals(command())) {
            return new SendMessage(chatId, "Выберите *страницу*, которую хотите перестать отслеживать.")
                .replyMarkup(InlineKeyboardBuilder.createCallbackKeyboard(List.of(
                    new Link(new UUID(1, 2), GITHUB_LINK),
                    new Link(new UUID(2, 2), STACK_LINK)
                )));
        }
        return new SendMessage(chatId, "Введите *только* команду /untrack.");
    }
}
