package com.epam.esm.web.controller;

import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ServiceException> handleServiceException(ServiceException exception) {
        if (exception.getMessage().contains("already exists")){
            return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ServiceException> handleConstraintViolationException(ConstraintViolationException e) {
        ServiceException myException = new ServiceException("not valid due to validation error: " + e.getMessage(),
                ErrorCodeEnum.INVALID_INPUT);
        return new ResponseEntity<>(myException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ServiceException> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getMessage();
        String splitString = "default message";
        int index = message.lastIndexOf(splitString);
        String myMessage = message.substring(index + splitString.length() + 2, message.length() - 3);

        ServiceException myException = new ServiceException("not valid due to validation error: " + myMessage,
                ErrorCodeEnum.INVALID_INPUT);
        return new ResponseEntity<>(myException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServiceException> handleIllegalArgumentException() {
        ServiceException myException = new ServiceException("Invalid param passed in a request",
                ErrorCodeEnum.INVALID_INPUT);
        return new ResponseEntity<>(myException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ServiceException> handleServiceException() {
        ServiceException exception = new ServiceException("Invalid field type passed in a JSON",
                ErrorCodeEnum.INVALID_INPUT);
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}
