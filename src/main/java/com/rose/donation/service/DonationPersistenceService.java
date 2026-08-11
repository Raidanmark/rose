package com.rose.donation.service;

import com.rose.common.exception.EntityNotFoundException;
import com.rose.donation.entity.Donation;
import com.rose.donation.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonationPersistenceService {
    private final DonationRepository donationRepository;

    @Transactional
    public Donation save(Donation donation) {
        return donationRepository.saveAndFlush(donation);
    }

    @Transactional
    public void attachProviderPayment(
            UUID donationId,
            String providerPaymentId
    ) {
        Donation donation =
                donationRepository.findById(donationId)
                        .orElseThrow(
                                () -> new EntityNotFoundException(
                                        "Donation not found for id: "
                                                + donationId
                                )
                        );

        donation.attachProviderPayment(
                providerPaymentId
        );
    }
}
