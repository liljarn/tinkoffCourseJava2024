package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.client.AddLinkRequest;
import edu.java.bot.dto.client.LinkResponse;
import edu.java.bot.exception.ScrapperException;
import edu.java.bot.service.command.CommandService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.MessageConstants.TRACK_COMMAND;
import static edu.java.bot.utils.MessageConstants.TRACK_COMMAND_ONLY;
import static edu.java.bot.utils.MessageConstants.TRACK_DESCRIPTION;
import static edu.java.bot.utils.MessageConstants.TRACK_WRONG_TEXT;
import static edu.java.bot.utils.MessageConstants.URL_START;

@Component
@RequiredArgsConstructor
public class CommandTrack implements Command {
    private final CommandService commandService;

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
            try {
                LinkResponse response = commandService.addLink(chatId, new AddLinkRequest(URI.create(data[1])));
                return new SendMessage(chatId, "Вебсайт " + response.url() + " теперь отслеживается.");
            } catch (ScrapperException e) {
                return new SendMessage(chatId, e.getMessage());
            }
        }
        return new SendMessage(chatId, TRACK_WRONG_TEXT);
    }

    private boolean isUrl(String url) {
        return url.startsWith(URL_START);
    }
}
