package com.bookmanagmentapp.bookmanagmentapplication.exceptions;

public class LogNotReadyException extends RuntimeException {
    public LogNotReadyException(String message) {
        super(message);
    }
}
