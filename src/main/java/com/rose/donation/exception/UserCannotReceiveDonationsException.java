package com.rose.donation.exception;

public class UserCannotReceiveDonationsException extends RuntimeException {
    public UserCannotReceiveDonationsException() {
        super("Creator cannot receive donations");
    }
}
