package edu.java.exceptions;

import org.springframework.http.HttpStatus;

public class LinkNotFoundException extends ScrapperException {
    public LinkNotFoundException() {
        super("Невозможно удалить ссылку", HttpStatus.NOT_FOUND, "Ссылка уже удалена");
    }
}
