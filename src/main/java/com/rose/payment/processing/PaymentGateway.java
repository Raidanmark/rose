package com.rose.payment.processing;

import com.rose.donation.entity.Donation;

public interface PaymentGateway {

    PaymentResult createPaymentIntent(Donation donation, String destinationAccountId);
}
