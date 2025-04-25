package com.bookmanagmentapp.bookmanagmentapplication.exceptions;

public class AuthorAlreadyExistsException extends RuntimeException {
    public AuthorAlreadyExistsException(String msg) {
        super(msg);
    }
}