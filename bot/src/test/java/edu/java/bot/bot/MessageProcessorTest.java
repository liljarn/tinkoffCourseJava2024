package edu.java.bot.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandStart;
import edu.java.bot.processor.MessageProcessor;
import edu.java.bot.processor.Processor;
import edu.java.bot.service.command.CommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mockito;
import java.util.List;
import static edu.java.bot.utils.MessageConstants.NOT_COMMAND;
import static edu.java.bot.utils.MessageConstants.NOT_TEXT;
import static edu.java.bot.utils.MessageConstants.START_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageProcessorTest {
    @Test
    @DisplayName("Not a command test")
    public void process_shouldReturnWrongMessage_whenCommandWasNotSent() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.doNothing().when(service).registerChat(1L);
        Update mockUpdate = TestUtils.createMockUpdate("smth", 1L);
        List<Command> commandsList = List.of(new CommandStart(service));
        Processor processor = new MessageProcessor(commandsList);
        //Act
        SendMessage message = processor.process(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(NOT_COMMAND);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Not a text test")
    public void process_shouldReturnWrongMessage_whenTextWasNotSent(String text) {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.doNothing().when(service).registerChat(1L);
        Update mockUpdate = TestUtils.createMockUpdate(text, 1L);
        List<Command> commandsList = List.of(new CommandStart(service));
        Processor processor = new MessageProcessor(commandsList);
        //Act
        SendMessage message = processor.process(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(NOT_TEXT);
    }

    @Test
    @DisplayName("Start command test")
    public void process_shouldReturnStartMessage_whenCommandStartWasSent() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.doNothing().when(service).registerChat(1L);
        Update mockUpdate = TestUtils.createMockUpdate("/start", 1L);
        List<Command> commandsList = List.of(new CommandStart(service));
        Processor processor = new MessageProcessor(commandsList);
        //Act
        SendMessage message = processor.process(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(START_MESSAGE);
    }
}
