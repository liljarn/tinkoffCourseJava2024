package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandTrack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static edu.java.bot.utils.MessageConstants.GITHUB_LINK;
import static edu.java.bot.utils.MessageConstants.TRACK_COMMAND;
import static edu.java.bot.utils.MessageConstants.TRACK_COMMAND_ONLY;
import static edu.java.bot.utils.MessageConstants.TRACK_WRONG_TEXT;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandTrackTest {
    @Test
    @DisplayName("Command /track test")
    public void handle_returnsMessage_whenOnlyCommandWasSent() {
        Update mockUpdate = TestUtils.createMockUpdate(TRACK_COMMAND, 1L);
        Command command = new CommandTrack();
        SendMessage message = command.handle(mockUpdate);
        assertThat(message.getParameters().get("text")).isEqualTo(TRACK_COMMAND_ONLY);
    }

    @Test
    @DisplayName("Command /track with link test")
    public void handle_returnsOkMessage_whenCommandAndLinkWereSent() {
        Update mockUpdate = TestUtils.createMockUpdate(TRACK_COMMAND + " " + GITHUB_LINK, 1L);
        Command command = new CommandTrack();
        SendMessage message = command.handle(mockUpdate);
        assertThat(message.getParameters().get("text"))
            .isEqualTo("Вебсайт " + GITHUB_LINK + " теперь отслеживается.");
    }

    @Test
    @DisplayName("Wrong command /track test")
    public void handle_returnsWrongMessage_whenWrongCommandWereSent() {
        Update mockUpdate = TestUtils.createMockUpdate(TRACK_COMMAND + "smth", 1L);
        Command command = new CommandTrack();
        SendMessage message = command.handle(mockUpdate);
        assertThat(message.getParameters().get("text"))
            .isEqualTo(TRACK_WRONG_TEXT);
    }

}
