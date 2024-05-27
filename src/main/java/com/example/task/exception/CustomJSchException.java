package com.example.task.exception;

import com.jcraft.jsch.JSchException;

public class CustomJSchException extends JSchException {
    public CustomJSchException(String message){
        super(message);
    }
}
