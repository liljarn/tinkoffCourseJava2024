package edu.java.bot.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processor.CallbackProcessor;
import edu.java.bot.processor.Processor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static edu.java.bot.utils.MessageConstants.SUCCESSFUL_DELETE;
import static org.assertj.core.api.Assertions.assertThat;

public class CallbackProcessorTest {
    @Test
    @DisplayName("Callback test")
    public void process_shouldReturnSuccessMessage_whenCallbackWasCalled() {
        //Arrange
        Update mockUpdate = TestUtils.createMockCallbackUpdate(1L);
        Processor processor = new CallbackProcessor();
        //Act
        SendMessage message = processor.process(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(SUCCESSFUL_DELETE);
    }
}
