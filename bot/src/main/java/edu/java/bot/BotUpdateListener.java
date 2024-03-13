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
import edu.java.bot.service.command.CommandService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BotUpdateListener implements UpdatesListener {
    private final Processor processor;
    private final Sender messageSender;

    public BotUpdateListener(List<Command> commandsList, Sender messageSender, CommandService service) {
        this.messageSender = messageSender;
        this.processor = AbstractProcessor.link(
            new MessageProcessor(commandsList),
            new CallbackProcessor(service)
        );
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            if (update.editedMessage() == null) {
                SendMessage sendMessage = processor.check(update);
                messageSender.sendMessage(sendMessage);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
