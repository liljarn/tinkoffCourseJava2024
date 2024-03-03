package edu.java.scrapper.controller;

import edu.java.controller.ChatController;
import edu.java.exceptions.ChatAlreadyRegisteredException;
import edu.java.exceptions.ChatNotAuthorizedException;
import edu.java.exceptions.ChatNotFoundException;
import edu.java.service.ChatService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {
    @MockBean
    private ChatService chatService;
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Correct request registerChat test")
    @SneakyThrows
    public void registerChat_shouldReturnOk_whenRequestIsCorrect() {
        Mockito.doNothing().when(chatService).registerChat(1L);
        mvc.perform(post("/tg-chat/1")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Correct request deleteChat test")
    @SneakyThrows
    public void deleteChat_shouldReturnOk_whenRequestIsCorrect() {
        Mockito.doNothing().when(chatService).deleteChat(1L);
        mvc.perform(delete("/tg-chat/1")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Non existing chat id deleteChat test")
    @SneakyThrows
    public void deleteChat_shouldReturnNotFound_whenChatIdDoesNotExist() {
        doThrow(new ChatNotFoundException(1000000L)).when(chatService).deleteChat(1000000L);
        mvc.perform(delete("/tg-chat/1000000")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Existing id register chat test")
    @SneakyThrows
    public void registerChat_shouldReturnConflict_whenChatExists() {
        doThrow(new ChatAlreadyRegisteredException()).when(chatService).registerChat(2L);
        mvc.perform(post("/tg-chat/2")).andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Unauthorized user delete chat test")
    @SneakyThrows
    public void deleteChat_shouldReturnUnauthorized_whenUserIsNotRegistered() {
        doThrow(new ChatNotAuthorizedException()).when(chatService).deleteChat(3L);
        mvc.perform(delete("/tg-chat/3")).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Bad endpoint delete chat test")
    @SneakyThrows
    public void deleteChat_shouldReturnBadRequest_whenEndPointIsNotCorrect() {
        mvc.perform(delete("/tg-chat/aaa")).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Bad endpoint register chat test")
    @SneakyThrows
    public void registerChat_shouldReturnBadRequest_whenEndPointIsNotCorrect() {
        mvc.perform(post("/tg-chat/aaa")).andExpect(status().isBadRequest());
    }
}
