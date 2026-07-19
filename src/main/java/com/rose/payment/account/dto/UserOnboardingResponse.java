package com.rose.payment.account.dto;

import com.rose.payment.account.entity.UserPaymentAccountStatus;

public record UserOnboardingResponse(
        UserPaymentAccountStatus status,
        String onboardingUrl
) {
}
