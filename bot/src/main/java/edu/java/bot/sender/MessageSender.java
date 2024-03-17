package edu.java.bot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessageSender implements Sender {
    private final TelegramBot bot;

    @Override
    public void sendMessage(SendMessage message) {
        SendResponse response = bot.execute(message.parseMode(ParseMode.HTML));
        log.info(response.isOk());
        log.info(response.description());
    }
}
