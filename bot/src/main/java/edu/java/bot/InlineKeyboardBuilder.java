package edu.java.bot;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import java.util.ArrayList;
import java.util.List;

public final class InlineKeyboardBuilder {
    private InlineKeyboardBuilder() {
    }

    public static InlineKeyboardMarkup createInlineKeyboard(List<String> linkTexts) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        for (String text : linkTexts) {
            InlineKeyboardButton button = new InlineKeyboardButton(text).url(text);
            buttons.add(button);
        }

        return new InlineKeyboardMarkup(buttons.toArray(new InlineKeyboardButton[0]));
    }
}
