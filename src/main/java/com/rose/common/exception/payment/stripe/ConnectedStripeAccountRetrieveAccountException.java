package com.rose.common.exception.payment.stripe;

import com.stripe.exception.StripeException;

public class ConnectedStripeAccountRetrieveAccountException extends RuntimeException {
    public ConnectedStripeAccountRetrieveAccountException(String message, StripeException exception) {
        super(message, exception);
    }
}
