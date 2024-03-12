package edu.java.bot.keyboard;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import edu.java.bot.dto.client.LinkResponse;
import java.util.List;

public final class InlineKeyboardBuilder {
    private InlineKeyboardBuilder() {
    }

    public static Keyboard createUrlKeyboard(List<LinkResponse> linkTexts) {
        return new InlineKeyboardMarkup(linkTexts.stream()
            .map(link -> new InlineKeyboardButton(link.url().toString()).url(link.url().toString()))
            .map(button -> new InlineKeyboardButton[] {button})
            .toArray(InlineKeyboardButton[][]::new));
    }

    public static Keyboard createCallbackKeyboard(List<LinkResponse> linkTexts) {
        return new InlineKeyboardMarkup(linkTexts.stream()
            .map(link -> new InlineKeyboardButton(link.url().toString()).callbackData(link.id().toString()))
            .map(button -> new InlineKeyboardButton[] {button})
            .toArray(InlineKeyboardButton[][]::new));
    }
}
