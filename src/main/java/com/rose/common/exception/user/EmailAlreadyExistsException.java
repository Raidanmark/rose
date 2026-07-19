package com.rose.common.exception.user;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super(email + "email already exists");
    }
}