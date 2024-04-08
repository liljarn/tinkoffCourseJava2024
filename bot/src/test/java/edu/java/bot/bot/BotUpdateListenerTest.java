package edu.java.bot.bot;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotUpdateListener;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandStart;
import edu.java.bot.processor.MessageProcessor;
import edu.java.bot.processor.Processor;
import edu.java.bot.sender.MessageSender;
import edu.java.bot.sender.Sender;
import edu.java.bot.service.command.CommandService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import static edu.java.bot.utils.MessageConstants.START_COMMAND;

public class BotUpdateListenerTest {
    private static final MeterRegistry METER_REGISTRY = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    @Test
    @DisplayName("Process sent message test")
    public void process_shouldCallMethodSendMessage_whenMessageWasSent() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.doNothing().when(service).registerChat(1L);
        Update mockUpdate = TestUtils.createMockUpdate(START_COMMAND, 1L);
        List<Command> commands = List.of(new CommandStart(service));
        List<Update> updates = List.of(mockUpdate);
        Sender sender = Mockito.mock(MessageSender.class);
        Processor processor = new MessageProcessor(commands, METER_REGISTRY);
        BotUpdateListener listener = new BotUpdateListener(processor, sender);
        //Act
        listener.process(updates);
        //Assert
        Mockito.verify(sender, Mockito.times(1))
            .sendMessage(Mockito.any());
    }

    @Test
    @DisplayName("Process edited message test")
    public void process_shouldNotCallMethodSendMessage_whenMessageWasEdited() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.doNothing().when(service).registerChat(1L);
        Update mockUpdate = TestUtils.createMockUpdateEditedMessage(START_COMMAND, 1L);
        List<Command> commands = List.of(new CommandStart(service));
        List<Update> updates = List.of(mockUpdate);
        Sender sender = Mockito.mock(MessageSender.class);
        Processor processor = new MessageProcessor(commands, METER_REGISTRY);
        BotUpdateListener listener = new BotUpdateListener(processor, sender);
        //Act
        listener.process(updates);
        //Assert
        Mockito.verify(sender, Mockito.times(0))
            .sendMessage(Mockito.any());
    }
}
