package edu.java.exceptions;

import org.springframework.http.HttpStatus;

public class ChatBadRequestException extends ScrapperException {
    public ChatBadRequestException() {
        super("Некорректные параметры запроса", HttpStatus.BAD_REQUEST, "Неправильно был введён запрос");
    }
}
