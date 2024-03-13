package edu.java.exceptions;

import org.springframework.http.HttpStatus;

public class LinkAlreadyTrackedException extends ScrapperException {
    public LinkAlreadyTrackedException() {
        super("Невозможно добавить ссылку", HttpStatus.CONFLICT, "Ссылка уже отcлеживается");
    }
}
