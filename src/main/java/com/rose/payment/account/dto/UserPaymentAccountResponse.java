package com.rose.payment.account.dto;

import com.rose.payment.account.entity.UserPaymentAccountStatus;

public record UserPaymentAccountResponse(

        UserPaymentAccountStatus status

) {
}
