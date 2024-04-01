package edu.java.bot.exception;

import org.springframework.http.HttpStatus;

public class ScrapperServiceUnavailableException extends ScrapperException {

    public ScrapperServiceUnavailableException() {
        super(
            "Подключение к Scrapper недоступно",
            HttpStatus.SERVICE_UNAVAILABLE,
            "К сожалению сервер сейчас <b>лежит</b>\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D"
        );
    }
}
