package edu.java.bot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSender implements Sender {
    private final TelegramBot bot;

    @Override
    public void sendMessage(SendMessage message) {
        bot.execute(message.parseMode(ParseMode.HTML));
    }
}
