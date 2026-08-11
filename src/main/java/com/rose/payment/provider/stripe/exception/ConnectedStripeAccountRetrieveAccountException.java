package com.rose.payment.provider.stripe.exception;

import com.stripe.exception.StripeException;

public class ConnectedStripeAccountRetrieveAccountException extends RuntimeException {
    public ConnectedStripeAccountRetrieveAccountException(String message, StripeException exception) {
        super(message, exception);
    }
}
