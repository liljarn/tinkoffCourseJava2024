package edu.java.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processor.UserMessageProcessor;
import edu.java.bot.sender.Sender;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class BotUpdateListener implements UpdatesListener {
    private final UserMessageProcessor messageProcessor;
    private final Sender messageSender;

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            log.info(update.message().text());
            SendMessage sendMessage = messageProcessor.process(update);
            messageSender.sendMessage(sendMessage);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
