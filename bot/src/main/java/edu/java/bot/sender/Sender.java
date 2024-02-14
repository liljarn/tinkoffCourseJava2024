package edu.java.bot.sender;

import com.pengrad.telegrambot.request.SendMessage;

public interface Sender {
    void sendMessage(SendMessage message);
}
