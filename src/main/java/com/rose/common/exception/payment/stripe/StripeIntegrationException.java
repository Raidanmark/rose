package com.rose.common.exception.payment.stripe;

import com.stripe.exception.StripeException;

import java.util.UUID;

public class StripeIntegrationException extends RuntimeException {
    public StripeIntegrationException(UUID donationId, StripeException exception) {
        super("Failed to create payment intent for donation: " + donationId, exception);
    }
}
