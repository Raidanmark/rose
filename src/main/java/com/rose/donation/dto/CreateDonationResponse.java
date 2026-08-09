package com.rose.donation.dto;

import com.rose.donation.entity.DonationStatus;

import java.util.UUID;

public record CreateDonationResponse(
        UUID donationId,
        DonationStatus status,
        String clientSecret
) {
}
