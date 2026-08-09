package com.rose.donation.mapper;

import com.rose.donation.dto.DonationResponse;
import com.rose.donation.entity.Donation;
import org.springframework.stereotype.Component;

@Component
public class DonationMapper {

    public DonationResponse toResponse(Donation donation) {
        return new DonationResponse(
                donation.getId(),
                donation.getSender() == null
                ? null : donation.getSender().getId(),
                donation.getRecipient().getId(),
                donation.getAmount(),
                donation.getCurrency(),
                donation.getMessage(),
                donation.getStatus(),
                donation.getCreatedAt()
        );
    }
}
