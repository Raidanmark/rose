package com.rose.common.exception.payment.stripe;

import java.util.UUID;

public class UserPaymentAccountNotFoundException extends RuntimeException {

    public UserPaymentAccountNotFoundException(UUID userId) {
        super("User payment account not found for user: " + userId);
    }
}
