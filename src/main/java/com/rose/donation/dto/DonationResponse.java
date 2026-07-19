package com.rose.donation.dto;

import com.rose.donation.entity.DonationCurrency;
import com.rose.donation.entity.DonationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DonationResponse(
        UUID id,
        UUID senderId,
        UUID recipientId,
        BigDecimal amount,
        DonationCurrency currency,
        String message,
        DonationStatus status,
        LocalDateTime createdAt
) {
}
