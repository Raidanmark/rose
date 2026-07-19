package com.rose.payment.stripe.connect;

import com.rose.user.entity.User;

public interface StripeConnectGateway {

    String createConnectedAccount(User user);

    String createOnboardingLink(String providerAccountId);

    StripeConnectedAccountSnapshot retrieveAccount(String providerAccountId);
}
