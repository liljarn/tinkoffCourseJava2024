package edu.java.bot.sender;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public interface Sender {
    SendResponse sendMessage(SendMessage message);
}
