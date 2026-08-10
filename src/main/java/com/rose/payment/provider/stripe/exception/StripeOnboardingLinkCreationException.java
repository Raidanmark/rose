package com.rose.payment.provider.stripe.exception;

import com.stripe.exception.StripeException;

public class StripeOnboardingLinkCreationException extends RuntimeException {
    public StripeOnboardingLinkCreationException(String message, StripeException exception) {
        super(message, exception);
    }
}
