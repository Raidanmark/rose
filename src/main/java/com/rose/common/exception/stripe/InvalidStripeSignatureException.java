package com.rose.common.exception.stripe;

import com.stripe.exception.StripeException;

public class InvalidStripeSignatureException extends RuntimeException {
    public InvalidStripeSignatureException(StripeException exception) {
        super(exception);
    }
}
