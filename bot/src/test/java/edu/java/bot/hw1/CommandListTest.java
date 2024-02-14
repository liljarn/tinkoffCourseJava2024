package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static edu.java.bot.utils.MessageConsts.LIST_COMMAND;
import static edu.java.bot.utils.MessageConsts.LIST_COMMANDS_TEXT;
import static edu.java.bot.utils.MessageConsts.LIST_WRONG_TEXT;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandListTest {
    @Test
    @DisplayName("Command /list test")
    public void handle_returnsListMessage_whenCommandIsCorrect() {
        Update mockUpdate = TestUtils.createMockUpdate(LIST_COMMAND, 1L);
        Command command = new CommandList();
        SendMessage message = command.handle(mockUpdate);
        assertThat(message.getParameters().get("text")).isEqualTo(LIST_COMMANDS_TEXT);
    }

    @Test
    @DisplayName("Wrong command /list test")
    public void handle_returnsWrongMessage_whenCommandIsWrong() {
        Update mockUpdate = TestUtils.createMockUpdate(LIST_COMMAND + "smth", 1L);
        Command command = new CommandList();
        SendMessage message = command.handle(mockUpdate);
        assertThat(message.getParameters().get("text")).isEqualTo(LIST_WRONG_TEXT);
    }
}
