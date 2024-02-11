package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processor.UserMessageProcessor;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CommandHelp implements Command {
    private final UserMessageProcessor messageProcessor;

    public CommandHelp(@Lazy UserMessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

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
        List<Command> commandList = messageProcessor.commands();
        StringBuilder stringBuilder = new StringBuilder("Список команд:\n");
        for (Command command : commandList) {
            stringBuilder.append(command.command()).append(" — ").append(command.description()).append('\n');
        }
        return new SendMessage(chatId, stringBuilder.toString());
    }
}
