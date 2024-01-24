package ru.yandex.javaemployeecalendar.error.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.yandex.javaemployeecalendar.error.exception.DataConflictException;
import ru.yandex.javaemployeecalendar.error.exception.EntityNotFoundException;
import ru.yandex.javaemployeecalendar.error.exception.InvalidRequestException;
import ru.yandex.javaemployeecalendar.error.model.ApiError;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(value = {
            HttpMessageNotReadableException.class,
            NoHandlerFoundException.class,
            MethodNotAllowedException.class,
            HttpRequestMethodNotSupportedException.class,
            NumberFormatException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            InvalidRequestException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final Exception e) {
        return new ApiError(null, e.getMessage(), "Incorrectly made request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            EntityNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotfoundException(final Exception exception) {
        return new ApiError(null, exception.getMessage(), "The required object was not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            DataConflictException.class,
            DataIntegrityViolationException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataConflictException(final Exception e) {
        return new ApiError(null, e.getMessage(), "Integrity constraint has been violated", HttpStatus.CONFLICT);
    }
}
