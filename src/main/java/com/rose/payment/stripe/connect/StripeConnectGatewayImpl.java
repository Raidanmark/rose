package com.rose.payment.stripe.connect;

import com.rose.common.exception.payment.stripe.StripeIntegrationException;
import com.rose.common.exception.payment.stripe.StripeOnboardingLinkCreationException;
import com.rose.payment.stripe.config.StripeProperties;
import com.rose.user.entity.User;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StripeConnectGatewayImpl implements StripeConnectGateway {

    private final StripeClient stripeClient;
    private final StripeProperties stripeProperties;

    @Override
    public String createConnectedAccount(User user) {
        AccountCreateParams params = AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.EXPRESS)
                .setEmail(user.getEmail())
                .putMetadata("rose_user_id", user.getId().toString())
                .setCapabilities(
                        AccountCreateParams.Capabilities.builder()
                                .setCardPayments(
                                        AccountCreateParams.Capabilities.CardPayments
                                                .builder()
                                                .setRequested(true)
                                                .build()
                                )
                                .setTransfers(
                                        AccountCreateParams.Capabilities.Transfers
                                                .builder()
                                                .setRequested(true)
                                                .build()
                                )
                                .build()
                )
                .build();

        try {
            return stripeClient.accounts().create(params).getId();
        } catch (StripeException exception) {
            throw new StripeIntegrationException(
                    user.getId(),
                    exception
            );
        }
    }

    @Override
    public String createOnboardingLink(String providerAccountId) {
        AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                .setAccount(providerAccountId)
                .setRefreshUrl(stripeProperties.onboardingRefreshUrl())
                .setReturnUrl(stripeProperties.onboardingReturnUrl())
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        try {
            AccountLink accountLink = stripeClient.accountLinks().create(params);
            return accountLink.getUrl();
        } catch (StripeException exception) {
            throw new StripeOnboardingLinkCreationException(
                    "Could not create Stripe onboarding link",
                    exception
            );
        }
    }

    @Override
    public StripeConnectedAccountSnapshot retrieveAccount(String providerAccountId) {

        try {
            Account account = stripeClient.accounts().retrieve(providerAccountId);

            return toSnapshot(account);
        } catch (StripeException exception) {
            throw new StripeOnboardingLinkCreationException(
                    "Could not retrieve Stripe connected account",
                    exception
            );
        }
    }

    public StripeConnectedAccountSnapshot toSnapshot(Account account) {
        boolean requirementsDue =
                account.getRequirements() != null
                        && account.getRequirements().getCurrentlyDue() != null
                        && !account.getRequirements().getCurrentlyDue().isEmpty();

        boolean disabled =
                account.getRequirements() != null
                        && account.getRequirements().getDisabledReason() != null
                        && !account.getRequirements().getDisabledReason().isBlank();

        return new StripeConnectedAccountSnapshot(
                account.getId(),
                Boolean.TRUE.equals(account.getChargesEnabled()),
                Boolean.TRUE.equals(account.getPayoutsEnabled()),
                requirementsDue,
                disabled
        );
    }
}
