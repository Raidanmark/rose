package com.rose.common.exception.user;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super(username + "user with this username already exists");
    }
}
