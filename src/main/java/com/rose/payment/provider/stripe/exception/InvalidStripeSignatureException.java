package com.rose.payment.provider.stripe.exception;

import com.stripe.exception.StripeException;

public class InvalidStripeSignatureException extends RuntimeException {
    public InvalidStripeSignatureException(StripeException exception) {
        super(exception);
    }
}
