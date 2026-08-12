package com.rose.payment.account.service;

import com.rose.common.exception.EntityNotFoundException;
import com.rose.donation.exception.UserCannotReceiveDonationsException;
import com.rose.payment.PaymentProvider;
import com.rose.payment.account.dto.UserOnboardingResponse;
import com.rose.payment.account.dto.UserPaymentAccountResponse;
import com.rose.payment.account.entity.UserPaymentAccount;
import com.rose.payment.account.entity.UserPaymentAccountStatus;
import com.rose.payment.account.repository.UserPaymentAccountRepository;
import com.rose.payment.provider.stripe.connect.StripeConnectGateway;
import com.rose.payment.provider.stripe.connect.StripeConnectedAccountSnapshot;
import com.rose.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPaymentAccountService {

    private final UserPaymentAccountRepository userPaymentAccountRepository;
    private final StripeConnectGateway stripeConnectGateway;

    public UserOnboardingResponse startOnboarding(User user ) {

        UserPaymentAccount paymentAccount = findOrCreatePaymentAccount(user);

        String onboardingUrl = stripeConnectGateway.createOnboardingLink(
                paymentAccount.getProviderAccountId()
        );

        return new UserOnboardingResponse(
                onboardingUrl
        );
    }

    public UserPaymentAccountResponse getStatus(User user) {

        Optional<UserPaymentAccount> accountOptional =
                userPaymentAccountRepository.findByUserId(user.getId());

        if (accountOptional.isEmpty()) {
            return new UserPaymentAccountResponse(
                    UserPaymentAccountStatus.NOT_CONNECTED
            );
        }

        UserPaymentAccount account = accountOptional.get();

        StripeConnectedAccountSnapshot snapshot = stripeConnectGateway
                .retrieveAccount(account.getProviderAccountId());

        return new UserPaymentAccountResponse(
                snapshot.status()
        );
    }

    @Transactional(readOnly = true)
    public UserPaymentAccount requireActiveAccount(UUID userId) {
        UserPaymentAccount account = userPaymentAccountRepository
                .findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserPaymentAccount not found for userId: " + userId));

        StripeConnectedAccountSnapshot snapshot =
                stripeConnectGateway.retrieveAccount(
                        account.getProviderAccountId()
                );

        if (!snapshot.canReceiveDonations()) {
            throw new UserCannotReceiveDonationsException();
        }

        return account;
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
        return userPaymentAccountRepository.findByUserId(user.getId())
                .orElseGet(() -> createPaymentAccountForUser(user));
    }
}
