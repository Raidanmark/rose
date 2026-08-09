package com.rose.payment.stripe.payment;

public record StripePaymentResult(
        String paymentIntentId,
        String clientSecret
) {
}
