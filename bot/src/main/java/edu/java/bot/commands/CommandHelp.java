package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandHelp implements Command {
    private final List<Command> commandsList;

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "выводит все команды и их назначение.";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        if (update.message().text().equals(command())) {
            StringBuilder stringBuilder = new StringBuilder("Список команд:\n");
            for (Command command : commandsList) {
                stringBuilder.append(command.command()).append(" — ").append(command.description()).append('\n');
            }
            return new SendMessage(chatId, stringBuilder.toString());
        }
        return new SendMessage(chatId, "Для вывода всех команд введите *только* команду /help.");
    }
}
