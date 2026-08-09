package com.rose.donation.dto;

import com.rose.donation.entity.DonationCurrency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateDonationRequest(

        @NotNull
        UUID recipientId,

        @NotNull
        @DecimalMin(value = "1.00")
        @Digits(integer = 7, fraction = 2)
        BigDecimal amount,

        @NotNull
        DonationCurrency currency,

        @Size(max = 255)
        String message
) {
}
