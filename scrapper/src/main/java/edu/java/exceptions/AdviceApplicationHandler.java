package edu.java.exceptions;

import edu.java.dto.response.ApiErrorResponse;
import java.util.Arrays;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class AdviceApplicationHandler {
    @ExceptionHandler(ScrapperException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse linkNotFound(ScrapperException e) {
        return new ApiErrorResponse(
            e.getDescription(),
            e.getCode().toString(),
            e.getClass().getName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(ScrapperException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse chatNotFound(ScrapperException e) {
        return new ApiErrorResponse(
            e.getDescription(),
            e.getCode().toString(),
            e.getClass().getName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(ScrapperException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse chatBadRequest(ScrapperException e) {
        return new ApiErrorResponse(
            e.getDescription(),
            e.getCode().toString(),
            e.getClass().getName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
