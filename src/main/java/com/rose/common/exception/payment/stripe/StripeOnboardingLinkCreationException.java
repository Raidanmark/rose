package com.rose.common.exception.payment.stripe;

import com.stripe.exception.StripeException;

public class StripeOnboardingLinkCreationException extends RuntimeException {
    public StripeOnboardingLinkCreationException(String message, StripeException exception) {
        super(message, exception);
    }
}
