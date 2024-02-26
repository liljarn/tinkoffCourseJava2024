package edu.java.exceptions;

import org.springframework.http.HttpStatus;

public class ChatNotFoundException extends ScrapperException {
    public ChatNotFoundException(long id) {
        super("Неверно введён id чата", HttpStatus.NOT_FOUND, "Чат %d не найден".formatted(id));
    }
}
