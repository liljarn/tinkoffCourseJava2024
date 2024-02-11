package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkTrackerBot {
    private final UpdatesListener updatesListener;
    private final TelegramBot bot;

    @PostConstruct
    private void start() {
        bot.setUpdatesListener(updatesListener);
    }
}
