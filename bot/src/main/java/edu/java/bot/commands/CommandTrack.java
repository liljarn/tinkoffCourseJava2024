package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.MessageConsts.TRACK_COMMAND;
import static edu.java.bot.utils.MessageConsts.TRACK_COMMAND_ONLY;
import static edu.java.bot.utils.MessageConsts.TRACK_DESCRIPTION;
import static edu.java.bot.utils.MessageConsts.TRACK_WRONG_TEXT;
import static edu.java.bot.utils.MessageConsts.URL_START;

@Component
public class CommandTrack implements Command {
    @Override
    public String command() {
        return TRACK_COMMAND;
    }

    @Override
    public String description() {
        return TRACK_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        String text = update.message().text();
        long chatId = update.message().chat().id();
        String[] data = text.split(" ");
        if (data.length == 1 && data[0].equals(command())) {
            return new SendMessage(chatId, TRACK_COMMAND_ONLY);
        } else if (data.length == 2 && isUrl(data[1])) {
            return new SendMessage(chatId, "Вебсайт " + text.split(" ")[1] + " теперь отслеживается.");
        }
        return new SendMessage(chatId, TRACK_WRONG_TEXT);
    }

    private boolean isUrl(String url) {
        return url.startsWith(URL_START);
    }
}
