package edu.java.exceptions;

import org.springframework.http.HttpStatus;

public class ChatNotAuthorizedException extends ScrapperException {
    public ChatNotAuthorizedException() {
        super(
            "Чат не зарегистрирован",
            HttpStatus.UNAUTHORIZED,
            "Вы не зарегистрированы: введите /start для начала работы"
        );
    }
}
