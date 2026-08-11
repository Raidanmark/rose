package com.rose.payment.account.exception;

import java.util.UUID;

public class UserPaymentAccountNotFoundException extends RuntimeException {

    public UserPaymentAccountNotFoundException(UUID userId) {
        super("User payment account not found for user: " + userId);
    }
}
