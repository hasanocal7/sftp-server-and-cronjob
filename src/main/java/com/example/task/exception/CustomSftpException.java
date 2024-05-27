package com.example.task.exception;

import com.jcraft.jsch.SftpException;

public class CustomSftpException extends SftpException {
    public CustomSftpException(Integer id, String message){
        super(id, message);
    }
}
