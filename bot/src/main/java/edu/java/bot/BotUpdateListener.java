package edu.java.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processor.Processor;
import edu.java.bot.sender.Sender;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BotUpdateListener implements UpdatesListener {
    private final Processor processor;
    private final Sender messageSender;

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            if (update.editedMessage() == null) {
                SendMessage sendMessage = processor.process(update);
                messageSender.sendMessage(sendMessage);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
