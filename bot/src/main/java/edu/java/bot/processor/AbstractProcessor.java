package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public abstract class AbstractProcessor implements Processor {
    private AbstractProcessor next;

    public static AbstractProcessor link(AbstractProcessor first, AbstractProcessor... chain) {
        AbstractProcessor head = first;
        for (AbstractProcessor nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    protected SendMessage checkNext(Update update) {
        if (next == null) {
            throw new RuntimeException("Bad request");
        }
        return next.check(update);
    }
}
