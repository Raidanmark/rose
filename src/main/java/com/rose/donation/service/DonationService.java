package com.rose.donation.service;

import com.rose.common.exception.EntityNotFoundException;
import com.rose.common.exception.donation.DonationAccessDeniedException;
import com.rose.donation.dto.DonationResponse;
import com.rose.donation.entity.Donation;
import com.rose.donation.mapper.DonationMapper;
import com.rose.donation.repository.DonationRepository;
import com.rose.payment.PaymentProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final DonationMapper donationMapper;

    @Transactional(readOnly = true)
    public DonationResponse getDonation(
            UUID donationId,
            UUID currentUserId
    ) {
        Donation donation = getById(donationId);

        ensureUserCanView(donation, currentUserId);

        return donationMapper.toResponse(donation);
    }

    @Transactional(readOnly = true)
    public Page<DonationResponse> getSentDonations(
            UUID userId,
            Pageable pageable
    ) {
        return donationRepository
                .findBySenderIdOrderByCreatedAtDesc(userId, pageable)
                .map(donationMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<DonationResponse> getReceivedDonations(
            UUID userId,
            Pageable pageable
    ) {
        return donationRepository.findByRecipientIdOrderByCreatedAtDesc(userId, pageable)
                .map(donationMapper::toResponse);
    }

    @Transactional
    public void markSucceeded(String providerPaymentId) {
        Donation donation = findByProviderPaymentId(providerPaymentId);
        donation.markSucceeded();
    }

    @Transactional
    public void markFailed(String providerPaymentId) {
        Donation donation = findByProviderPaymentId(providerPaymentId);
        donation.markFailed();
    }

    @Transactional
    public void markCanceled(String providerPaymentId) {
        Donation donation = findByProviderPaymentId(providerPaymentId);
        donation.markCanceled();
    }

    private Donation getById(UUID donationId) {
        return donationRepository.findById(donationId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Donation not found for id: " + donationId
                        )
                );
    }

    private Donation findByProviderPaymentId(
            String providerPaymentId
    ) {
        return donationRepository
                .findByPaymentProviderAndProviderPaymentId(
                        PaymentProvider.STRIPE.toString(),
                        providerPaymentId
                )
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Donation for Stripe PaymentIntent not found: "
                                        + providerPaymentId
                        )
                );
    }

    private void ensureUserCanView(
            Donation donation,
            UUID currentUserId
    ) {
        boolean recipient =
                donation.getRecipient().getId().equals(currentUserId);

        boolean sender =
                donation.getSender() != null
                        && donation.getSender()
                        .getId()
                        .equals(currentUserId);

        if (!recipient && !sender) {
            throw new DonationAccessDeniedException();
        }
    }
}
