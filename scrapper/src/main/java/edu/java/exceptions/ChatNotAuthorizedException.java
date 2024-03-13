package edu.java.exceptions;

import org.springframework.http.HttpStatus;

public class ChatNotAuthorizedException extends ScrapperException {
    public ChatNotAuthorizedException() {
        super("Чат незарегистрирован", HttpStatus.UNAUTHORIZED, "Нельзя вводить команды до регистрации");
    }
}
