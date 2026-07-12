package com.rose.common.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super(username + "user with this username already exists");
    }
}
