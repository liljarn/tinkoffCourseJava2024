package edu.java.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.update.UpdateService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.net.URI;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateController.class)
public class UpdateControllerTest {
    //Given
    @MockBean UpdateService updateService;
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("Correct request updateLink test")
    @SneakyThrows
    public void updateLink_shouldReturnOk_whenRequestIsCorrect() {
        //Arrange
        LinkUpdate linkUpdate = new LinkUpdate(3L, URI.create("google.com"), "test", List.of(1L));
        Mockito.doNothing().when(updateService).updateLink(linkUpdate);
        //Act
        var act = mvc.perform(MockMvcRequestBuilders.post("/updates").contentType("application/json")
            .content(objectMapper.writeValueAsString(linkUpdate)));
        //Assert
        act.andExpect(status().isOk());
    }

    @Test
    @DisplayName("No body updateLink test")
    @SneakyThrows
    public void updateLink_shouldReturnBadRequest_whenNoBody() {
        //Arrange
        LinkUpdate linkUpdate = new LinkUpdate(3L, URI.create("google.com"), "test", List.of(1L));
        Mockito.doNothing().when(updateService).updateLink(linkUpdate);
        //Act
        var act = mvc.perform(MockMvcRequestBuilders.post("/updates").contentType("application/json"));
        //Assert
        act.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Wrong body updateLink test")
    @SneakyThrows
    public void updateLink_shouldReturnBadRequest_whenWrongBody() {
        //Arrange
        LinkUpdate linkUpdate = new LinkUpdate(3L, URI.create("google.com"), "test", List.of(1L));
        Mockito.doNothing().when(updateService).updateLink(linkUpdate);
        //Act
        var act = mvc.perform(MockMvcRequestBuilders.post("/updates").contentType("application/json")
            .content("{}"));
        //Assert
        act.andExpect(status().isBadRequest());
    }
}
