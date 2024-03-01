package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandUntrack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static edu.java.bot.utils.MessageConstants.UNTRACK_COMMAND;
import static edu.java.bot.utils.MessageConstants.UNTRACK_MESSAGE;
import static edu.java.bot.utils.MessageConstants.UNTRACK_WRONG_TEXT;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandUntrackTest {
    @Test
    @DisplayName("Command /untrack test")
    public void handle_returnsMessage_whenOnlyCommandWasSent() {
        Update mockUpdate = TestUtils.createMockUpdate(UNTRACK_COMMAND, 1L);
        Command command = new CommandUntrack();
        SendMessage message = command.handle(mockUpdate);
        assertThat(message.getParameters().get("text")).isEqualTo(UNTRACK_MESSAGE);
    }

    @Test
    @DisplayName("Wrong command /untrack test")
    public void handle_returnsWrongMessage_whenWrongCommandWasSent() {
        Update mockUpdate = TestUtils.createMockUpdate(UNTRACK_COMMAND + "smth", 1L);
        Command command = new CommandUntrack();
        SendMessage message = command.handle(mockUpdate);
        assertThat(message.getParameters().get("text")).isEqualTo(UNTRACK_WRONG_TEXT);
    }
}
