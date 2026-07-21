package com.rose.payment.account.service;

import com.rose.common.exception.EntityNotFoundException;
import com.rose.common.exception.donation.UserCannotReceiveDonationsException;
import com.rose.payment.PaymentProvider;
import com.rose.payment.account.dto.UserOnboardingResponse;
import com.rose.payment.account.entity.UserPaymentAccount;
import com.rose.payment.account.repository.UserPaymentAccountRepository;
import com.rose.payment.stripe.connect.StripeConnectGateway;
import com.rose.payment.stripe.connect.StripeConnectedAccountSnapshot;
import com.rose.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPaymentAccountService {

    private final UserPaymentAccountRepository userPaymentAccountRepository;
    private final StripeConnectGateway stripeConnectGateway;

    @Transactional
    public UserOnboardingResponse startOnboarding(User user ) {

        UserPaymentAccount paymentAccount = findOrCreatePaymentAccount(user);

        synchronizeFromStripe(paymentAccount);

        if (paymentAccount.canReceiveDonations()) {
            return new UserOnboardingResponse(
                    paymentAccount.getStatus(),
                    null
            );
        }

        String onboardingUrl = stripeConnectGateway.createOnboardingLink(
                paymentAccount.getProviderAccountId()
        );

        return new UserOnboardingResponse(
                paymentAccount.getStatus(),
                onboardingUrl
        );
    }

    @Transactional(readOnly = true)
    public UserPaymentAccount requireActiveAccount(UUID userId) {
        UserPaymentAccount account = userPaymentAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserPaymentAccount not found for userId: " + userId));

        if (!account.canReceiveDonations()) {
            throw new UserCannotReceiveDonationsException();
        }

        return account;
    }

    @Transactional
    public void synchronizeFromWebhook(
            String providerAccountId,
            boolean chargesEnabled,
            boolean payoutsEnabled,
            boolean disabled
    ) {
        UserPaymentAccount account = userPaymentAccountRepository
                .findByProviderAndProviderAccountId(
                        PaymentProvider.STRIPE,
                        providerAccountId
                )
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "UserPaymentAccount not found for providerAccountId: " + providerAccountId
                        )
                );

        account.synchronize(chargesEnabled, payoutsEnabled, disabled);
    }

    private void synchronizeFromStripe(UserPaymentAccount paymentAccount) {
        StripeConnectedAccountSnapshot snapshot = stripeConnectGateway.retrieveAccount(
                paymentAccount.getProviderAccountId());

        paymentAccount.synchronize(
            snapshot.chargesEnabled(),
            snapshot.payoutsEnabled(),
            snapshot.disabled()
        );

            userPaymentAccountRepository.save(paymentAccount);

    }

    private UserPaymentAccount createPaymentAccountForUser(User user) {
        String stripeAccountId = stripeConnectGateway.createConnectedAccount(user);

        UserPaymentAccount account = UserPaymentAccount.create(
                user,
                PaymentProvider.STRIPE,
                stripeAccountId
        );

        return userPaymentAccountRepository.save(account);
    }

    private UserPaymentAccount findOrCreatePaymentAccount(User user) {
        return userPaymentAccountRepository.findById(user.getId())
                .orElseGet(() -> createPaymentAccountForUser(user));
    }
}
