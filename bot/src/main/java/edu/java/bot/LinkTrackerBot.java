package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import edu.java.bot.menu.CommandMenuBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkTrackerBot {
    private final UpdatesListener updatesListener;
    private final TelegramBot bot;
    private final CommandMenuBuilder commandMenuBuilder;

    @PostConstruct
    private void start() {
        bot.setUpdatesListener(updatesListener);
        bot.execute(commandMenuBuilder.getCommandsMenu());
    }
}
