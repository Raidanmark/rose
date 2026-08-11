package com.rose.payment.provider.stripe.connect;

public record StripeConnectedAccountSnapshot(
        String accountId,
        boolean chargesEnabled,
        boolean payoutsEnabled,
        boolean requirementsDue,
        boolean disabled
) {
}
