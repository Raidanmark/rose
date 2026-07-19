package com.rose.payment.stripe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
public record StripeProperties(
        String secretKey,
        String webhookSecret,
        String onboardingRefreshUrl,
        String onboardingReturnUrl
) {
}
