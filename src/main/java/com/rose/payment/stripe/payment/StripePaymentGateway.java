package com.rose.payment.stripe.payment;

import com.rose.donation.entity.Donation;

public interface StripePaymentGateway {

    StripePaymentResult createPaymentIntent(Donation donation, String destinationAccountId);
}
