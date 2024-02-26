package edu.java.controller;

import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/{id}")
    public void registerChat(@PathVariable("id") Long chatId) {
        chatService.registerChat(chatId);
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable("id") Long chatId) {
        chatService.deleteChat(chatId);
    }
}
