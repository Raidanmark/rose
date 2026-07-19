package com.rose.payment.stripe.connect;

public record StripeConnectedAccountSnapshot(
        String accountId,
        boolean chargesEnabled,
        boolean payoutsEnabled,
        boolean requirementsDue,
        boolean disabled
) {
}
