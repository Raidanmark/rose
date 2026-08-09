package com.rose.payment.processing;

public record PaymentResult(
        String paymentIntentId,
        String clientSecret
) {
}
