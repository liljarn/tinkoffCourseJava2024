package edu.java.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ScrapperException extends RuntimeException {
    private final String description;
    private final HttpStatus code;

    public ScrapperException(
        String description,
        HttpStatus code,
        String message
    ) {
        super(message);
        this.description = description;
        this.code = code;
    }
}
