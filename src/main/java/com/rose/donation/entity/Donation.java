package com.rose.donation.entity;

import com.rose.payment.PaymentProvider;
import com.rose.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "donations",
        indexes = {
            @Index(
                    name = "idx_donations_recipient_created_at",
                    columnList = "recipient_id, created_at"
            ),
            @Index(
                    name = "idx_donations_sender_created_at",
                    columnList = "sender_id, created_at"
            ),
            @Index(
                    name = "idx_donations_status",
                    columnList = "status"
            )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_donations_provider_payment",
                        columnNames = {
                                "payment_provider",
                                "provider_payment_id"
                        }
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Donation {

    private static final int MONEY_SCALE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch  = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch  = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false, updatable = false)
    private User recipient;

    @Column(name = "amount",
            nullable = false,
            precision = 10,
            scale = MONEY_SCALE,
            updatable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 3,
            updatable = false
    )
    private DonationCurrency currency;

    @Column(length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DonationStatus status;

    @Column(name = "payment_provider", length = 50)
    private String paymentProvider;

    @Column(name = "provider_payment_id", length = 255)
    private String providerPaymentId;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    public Donation(
            User sender,
            User recipient,
            BigDecimal amount,
            DonationCurrency currency,
            String message
    ) {
        this.sender = sender;
        this.recipient = Objects.requireNonNull(recipient);
        this.amount = validateAmount(amount);
        this.currency = Objects.requireNonNull(currency);
        this.message = normalizeMessage(message);
        this.status = DonationStatus.PENDING;
        this.paymentProvider = PaymentProvider.STRIPE.toString();
    }


    public void attachProviderPayment(String providerPaymentId) {
        if (this.providerPaymentId != null) {
            throw new IllegalStateException(
                    "Provider payment is already attached"
            );
        }

        if (providerPaymentId == null || providerPaymentId.isBlank()) {
            throw new IllegalArgumentException(
                    "Provider payment ID must not be blank"
            );
        }

        this.providerPaymentId = providerPaymentId;
    }

    public void markSucceeded() {
        if (status == DonationStatus.SUCCEEDED) {
            return;
        }

        requirePending();
        status = DonationStatus.SUCCEEDED;
    }

    public void markFailed() {
        if (status == DonationStatus.FAILED) {
            return;
        }

        requirePending();
        status = DonationStatus.FAILED;
    }

    public void markCanceled() {
        if (status == DonationStatus.CANCELED) {
            return;
        }

        requirePending();
        status = DonationStatus.CANCELED;
    }

    private void requirePending() {
        if (status != DonationStatus.PENDING) {
            throw new IllegalStateException(
                    "Donation status transition is not allowed: " + status
            );
        }
    }

    private static BigDecimal validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Donation amount must be positive"
            );
        }

        return amount.setScale(2);
    }

    private static String normalizeMessage(String message) {
        if (message == null || message.isBlank()) {
            return null;
        }

        return message.trim();
    }
}
