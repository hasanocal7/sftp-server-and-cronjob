package com.example.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(CustomJSchException.class)
    public ResponseEntity<String> handleJSchException(CustomJSchException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getLocalizedMessage());
    }

    @ExceptionHandler(CustomSftpException.class)
    public ResponseEntity<String> handleSftpException(CustomSftpException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getLocalizedMessage());
    }
}
