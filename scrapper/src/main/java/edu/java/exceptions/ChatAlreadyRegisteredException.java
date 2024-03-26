package edu.java.exceptions;

import org.springframework.http.HttpStatus;

public class ChatAlreadyRegisteredException extends ScrapperException {
    public ChatAlreadyRegisteredException() {
        super("Невозможно зарегистрировать пользователя", HttpStatus.CONFLICT, "Пользователь уже зарегистрирован");
    }
}
