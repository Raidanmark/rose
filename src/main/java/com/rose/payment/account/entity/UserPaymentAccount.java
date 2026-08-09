package com.rose.payment.account.entity;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(
        name = "user_payment_accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_payment_accounts_user_id_provider",
                        columnNames = "user_id"

                )
        }
)
public class UserPaymentAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private PaymentProvider provider;

    @Column(name = "provider_account_id", nullable = false)
    private String providerAccountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private UserPaymentAccountStatus status;

    @Column(name = "charges_enabled", nullable = false)
    private boolean chargesEnabled;

    @Column(name = "payouts_enabled", nullable = false)
    private boolean payoutsEnabled;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static UserPaymentAccount create(
            User user,
            PaymentProvider provider,
            String providerAccountId
    ) {
        UserPaymentAccount account = new UserPaymentAccount();
        account.user = user;
        account.provider = provider;
        account.providerAccountId = providerAccountId;
        account.status = UserPaymentAccountStatus.ONBOARDING;
        account.chargesEnabled = false;
        account.payoutsEnabled = false;
        return account;
    }

    public void synchronize(
            boolean chargeEnabled,
            boolean payoutsEnabled,
            boolean disabled
    ) {
        this.chargesEnabled = chargeEnabled;
        this.payoutsEnabled = payoutsEnabled;

        if (disabled) {
            this.status = UserPaymentAccountStatus.DISABLED;
        } else if (chargeEnabled && payoutsEnabled) {
            this.status = UserPaymentAccountStatus.ACTIVE;
        } else {
            this.status = UserPaymentAccountStatus.ONBOARDING;
        }
    }

    public boolean canReceiveDonations() {
        return status == UserPaymentAccountStatus.ACTIVE
                && chargesEnabled
                && payoutsEnabled;
    }
}
