package edu.java.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.processor.AbstractProcessor;
import edu.java.bot.processor.CallbackProcessor;
import edu.java.bot.processor.MessageProcessor;
import edu.java.bot.processor.Processor;
import edu.java.bot.sender.Sender;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BotUpdateListener implements UpdatesListener {
    private final Processor processor;
    private final Sender messageSender;

    public BotUpdateListener(List<Command> commandsList, Sender messageSender) {
        this.messageSender = messageSender;
        this.processor = AbstractProcessor.link(
            new MessageProcessor(commandsList),
            new CallbackProcessor()
        );
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            log.info(update.message());
            log.info(update.callbackQuery());
            SendMessage sendMessage = processor.check(update);
            messageSender.sendMessage(sendMessage);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
