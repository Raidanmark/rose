package com.rose.payment.account.dto;

public record UserPaymentAccountResponse(
        boolean connected,
        UserPaymentAccountResponse status,
        boolean chargesEnabled,
        boolean requirementsDue,
        boolean canReceivePayments
) {
}
