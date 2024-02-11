package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class CommandTrack implements Command {
    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "отслеживает изменения на страницах GitHub и StackOverflow."
        + " Для отслеживания страницы введите /track и ссылку на неё.";
    }

    @Override
    public SendMessage handle(Update update) {
        String text = update.message().text();
        long chatId = update.message().chat().id();
        if (text.equals(command())) {
            return new SendMessage(
                chatId,
                "Введите команду /track и **ссылку** на ресурс."
            );
        } else if (text.split(" ").length == 2) {
            return new SendMessage(chatId, "Вебсайт " + text.split(" ")[1] + " теперь отслеживается.");
        }
        return new SendMessage(
            chatId,
            "**Неверно** введена команда /track. Раздельно введите команду /track и ссылку на ресурс."
        );
    }
}
