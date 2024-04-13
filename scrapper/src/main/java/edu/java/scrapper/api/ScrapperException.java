package edu.java.scrapper.api;

import edu.java.models.Response.ApiErrorResponse;
import edu.java.scrapper.exceptions.AlreadyExistException;
import edu.java.scrapper.exceptions.NotExistException;
import edu.java.scrapper.exceptions.RepeatedRegistrationException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@SuppressWarnings("MultipleStringLiterals")
@RestControllerAdvice
public class ScrapperException {
    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> exceptionBadRequest(AlreadyExistException exception) {
        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(
                "Некорректные параметры запроса",
                "400",
                exception.getClass().getName(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)
            ));
    }

    @ExceptionHandler(RepeatedRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> exceptionBadRequest(Exception exception) {
        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(
                "Некорректные параметры запроса",
                "400",
                exception.getClass().getName(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)
            ));
    }

    @ExceptionHandler(NotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> exceptionNotFound(NotExistException exception) {
        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(
                "Чата/Ссылки не существует",
                "404",
                exception.getClass().getName(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)
            ));
    }
}
