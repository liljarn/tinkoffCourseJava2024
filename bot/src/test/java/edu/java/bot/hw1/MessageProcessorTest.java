package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandStart;
import edu.java.bot.processor.MessageProcessor;
import edu.java.bot.processor.Processor;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import static edu.java.bot.utils.MessageConstants.NOT_COMMAND;
import static edu.java.bot.utils.MessageConstants.NOT_TEXT;
import static edu.java.bot.utils.MessageConstants.START_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageProcessorTest {
    @Test
    @DisplayName("Not a command test")
    public void process_shouldReturnWrongMessage_whenCommandWasNotSent() {
        Update mockUpdate = TestUtils.createMockUpdate("smth", 1L);
        List<Command> commandsList = List.of(new CommandStart());
        Processor processor = new MessageProcessor(commandsList);
        SendMessage message = processor.process(mockUpdate);
        assertThat(message.getParameters().get("text")).isEqualTo(NOT_COMMAND);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Not a text test")
    public void process_shouldReturnWrongMessage_whenTextWasNotSent(String text) {
        Update mockUpdate = TestUtils.createMockUpdate(text, 1L);
        List<Command> commandsList = List.of(new CommandStart());
        Processor processor = new MessageProcessor(commandsList);
        SendMessage message = processor.process(mockUpdate);
        assertThat(message.getParameters().get("text")).isEqualTo(NOT_TEXT);
    }

    @Test
    @DisplayName("Not a command test")
    public void process_shouldReturnWrongMessage_whenCommandWas() {
        Update mockUpdate = TestUtils.createMockUpdate("/start", 1L);
        List<Command> commandsList = List.of(new CommandStart());
        Processor processor = new MessageProcessor(commandsList);
        SendMessage message = processor.process(mockUpdate);
        assertThat(message.getParameters().get("text")).isEqualTo(START_MESSAGE);
    }
}
