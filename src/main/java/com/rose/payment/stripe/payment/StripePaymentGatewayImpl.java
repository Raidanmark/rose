package com.rose.payment.stripe.payment;

import com.rose.common.exception.payment.stripe.StripeIntegrationException;
import com.rose.donation.entity.Donation;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StripePaymentGatewayImpl implements StripePaymentGateway {

    private final StripeClient stripeClient;

    @Override
    public StripePaymentResult createPaymentIntent(Donation donation, String destinationAccountId) {

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(toMinorUnits(donation))
                .setCurrency(donation.getCurrency().toStripeCurrency())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
                .putMetadata(
                        "recipient_id",
                        donation.getRecipient().getId().toString()
                )

                .putMetadata("donationId", donation.getId().toString())
                .setTransferData(
                        PaymentIntentCreateParams.TransferData.builder()
                                .setDestination(destinationAccountId)
                                .build()
                )
                .build();

        RequestOptions requestOptions = RequestOptions.builder()
                .setIdempotencyKey("donation_" + donation.getId())
                .build();

        try {
            PaymentIntent paymentIntent = stripeClient
                    .paymentIntents()
                    .create(params, requestOptions);

            return new StripePaymentResult(
                    paymentIntent.getId(),
                    paymentIntent.getClientSecret()
            );

        } catch (StripeException  exception) {
            throw  new StripeIntegrationException(donation.getId(), exception);
        }
    }

    private long toMinorUnits(Donation donation) {
        return donation.getAmount()
                .movePointRight(2)
                .longValueExact();
    }
}
