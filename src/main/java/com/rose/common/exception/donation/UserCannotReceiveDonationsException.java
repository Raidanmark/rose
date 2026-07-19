package com.rose.common.exception.donation;

public class UserCannotReceiveDonationsException extends RuntimeException {
    public UserCannotReceiveDonationsException() {
        super("Creator cannot receive donations");
    }
}
