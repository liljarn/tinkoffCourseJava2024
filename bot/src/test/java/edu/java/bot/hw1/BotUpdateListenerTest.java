package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotUpdateListener;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandStart;
import edu.java.bot.sender.MessageSender;
import edu.java.bot.sender.Sender;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static edu.java.bot.utils.MessageConstants.START_COMMAND;

public class BotUpdateListenerTest {
    @Test
    @DisplayName("Process test")
    public void process_shouldCallMethodSendMessage_fromSenderClass() {
        Update mockUpdate = TestUtils.createMockUpdate(START_COMMAND, 1L);
        List<Command> commands = List.of(new CommandStart());
        List<Update> updates = List.of(mockUpdate);
        Sender sender = Mockito.mock(MessageSender.class);
        BotUpdateListener listener = new BotUpdateListener(commands, sender);
        listener.process(updates);
        Mockito.verify(sender, Mockito.times(1))
            .sendMessage(Mockito.any());
    }
}
