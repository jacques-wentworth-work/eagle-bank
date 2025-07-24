package com.eaglebank.exception;

import com.eaglebank.exception.resource.GenericError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GenericError> handleRuntime(RuntimeException ex) {
        return new ResponseEntity<>(new GenericError(ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericError> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errorDetails = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .sorted(Comparator.comparing(FieldError::getField))
                .map(error -> {
                    Map<String, String> errorDetail = new HashMap<>();
                    errorDetail.put("field", error.getField());
                    errorDetail.put("message", error.getDefaultMessage());
                    errorDetail.put("type", error.getCode());
                    return errorDetail;
                })
                .toList();

        return new ResponseEntity<>(new GenericError("Validation failed", errorDetails), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<GenericError> handleDuplicateEmail(DuplicateEmailException ex) {
        return new ResponseEntity<>(new GenericError(ex.getMessage(), null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GenericError> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(new GenericError(ex.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<GenericError> handleAccountNotFoundException(AccountNotFoundException ex) {
        return new ResponseEntity<>(new GenericError(ex.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<GenericError> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        return new ResponseEntity<>(new GenericError(ex.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserLoginException.class)
    public ResponseEntity<GenericError> handleUserLoginException(UserLoginException ex) {
        return new ResponseEntity<>(new GenericError(ex.getMessage(), null), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessForbiddenException.class)
    public ResponseEntity<GenericError> handleAccessForbiddenException(AccessForbiddenException  ex) {
        return new ResponseEntity<>(new GenericError(ex.getMessage(), null), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnprocessedTransactionException.class)
    public ResponseEntity<GenericError> handleUnprocessedTransactionException(UnprocessedTransactionException  ex) {
        return new ResponseEntity<>(new GenericError(ex.getMessage(), null), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
