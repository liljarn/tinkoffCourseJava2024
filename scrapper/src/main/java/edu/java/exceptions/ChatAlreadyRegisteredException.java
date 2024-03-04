package edu.java.exceptions;

import org.springframework.http.HttpStatus;

public class ChatAlreadyRegisteredException extends ScrapperException {
    public ChatAlreadyRegisteredException() {
        super("Пользователь уже зарегистрирован", HttpStatus.CONFLICT, "Невозможно зарегистрировать пользователя");
    }
}
