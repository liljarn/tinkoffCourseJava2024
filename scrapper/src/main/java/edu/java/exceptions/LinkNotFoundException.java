package edu.java.exceptions;

import edu.java.dto.request.AddLinkRequest;
import org.springframework.http.HttpStatus;

public class LinkNotFoundException extends ScrapperException {
    public LinkNotFoundException(AddLinkRequest addLinkRequest) {
        super("Ссылка не найдена", HttpStatus.NOT_FOUND, "Ссылка %s не была найдена".formatted(addLinkRequest.link()));
    }
}
