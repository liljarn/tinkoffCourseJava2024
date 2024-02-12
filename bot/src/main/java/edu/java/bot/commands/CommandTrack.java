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
        String[] data = text.split(" ");
        if (data.length == 1) {
            return new SendMessage(
                chatId,
                "Введите команду /track и *ссылку* на ресурс."
            );
        } else if (data.length == 2 && isUrl(data[1])) {
            return new SendMessage(chatId, "Вебсайт " + text.split(" ")[1] + " теперь отслеживается.");
        }
        return new SendMessage(
            chatId,
            "*Неверно* введена *команда* /track или *сслыка*. Введите команду /track и ссылку на ресурс раздельно."
                + " Ссылка должна начинаться с https://"
        );
    }

    private boolean isUrl(String url) {
        return url.startsWith("https://");
    }
}
