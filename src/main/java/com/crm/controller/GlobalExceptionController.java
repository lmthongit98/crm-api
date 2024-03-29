package com.crm.controller;

import com.crm.dto.ErrorDetails;
import com.crm.exception.BadRequestException;
import com.crm.exception.ResourceNotFoundException;
import com.crm.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionController {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest) {
        logger.error("ResourceNotFoundException", ex);
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(BadRequestException ex, WebRequest webRequest) {
        logger.error("BadRequestException", ex);
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDetails> handleUnauthorizedException(UnauthorizedException ex, WebRequest webRequest) {
        logger.error("UnauthorizedException", ex);
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> conflictData(Exception ex, WebRequest webRequest) {
        logger.error("SQLIntegrityConstraintViolationException", ex);
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDetails> methodNotSupportedException(Exception ex, WebRequest webRequest) {
        logger.error("HttpRequestMethodNotSupportedException", ex);
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDetails> badRequestHandler(Exception ex, WebRequest webRequest) {
        logger.error("BadRequestException", ex);
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class, IllegalStateException.class})
    public ResponseEntity<ErrorDetails> handleUnhandledException(Exception ex, WebRequest webRequest) {
        logger.error("UnhandledException", ex);
        String message = "There was some error in processing your request. An exception has been logged";
        ErrorDetails errorDetails = new ErrorDetails(new Date(), message, webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
