package com.rose.donation.repository;

import com.rose.donation.entity.Donation;
import com.rose.payment.PaymentProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DonationRepository extends JpaRepository<Donation, UUID> {

    Page<Donation> findByRecipientIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    Page<Donation> findBySenderIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    Optional<Donation> findByPaymentProviderAndProviderPaymentId(String paymentProvider, String donationId);
}
