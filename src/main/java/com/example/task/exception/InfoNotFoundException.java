package com.example.task.exception;

public class InfoNotFoundException extends RuntimeException{
    public InfoNotFoundException(String message){
        super(message);
    }
}
