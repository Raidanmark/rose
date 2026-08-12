package com.rose.payment.provider.stripe.connect;

import com.rose.payment.account.entity.UserPaymentAccountStatus;

public record StripeConnectedAccountSnapshot(
        String accountId,
        boolean chargesEnabled,
        boolean payoutsEnabled,
        boolean requirementsDue,
        boolean disabled
) {
    public UserPaymentAccountStatus status() {
        if (disabled) {
            return UserPaymentAccountStatus.RESTRICTED;
        }

        if (requirementsDue) {
            return UserPaymentAccountStatus.ONBOARDING_REQUIRED;
        }

        if (chargesEnabled && payoutsEnabled) {
            return UserPaymentAccountStatus.ACTIVE;
        }

        return UserPaymentAccountStatus.VERIFYING;
    }

    public boolean canReceiveDonations() {
        return status() == UserPaymentAccountStatus.ACTIVE;
    }
}
