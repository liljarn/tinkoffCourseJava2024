package edu.java.exceptions;

import org.springframework.http.HttpStatus;

public class LinkAlreadyTrackedException extends ScrapperException {
    public LinkAlreadyTrackedException() {
        super("Ссылка уже отcлеживается", HttpStatus.CONFLICT, "Невозможно добавить ссылку");
    }
}
