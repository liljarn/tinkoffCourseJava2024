package edu.java.exceptions;

import java.net.URI;
import org.springframework.http.HttpStatus;

public class LinkNotSupportedException extends ScrapperException {
    public LinkNotSupportedException(URI link) {
        super(
            "Ссылка не поддерживается",
            HttpStatus.BAD_REQUEST,
            "Cсылка %s не поддерживается".formatted(link.toString())
        );
    }
}
