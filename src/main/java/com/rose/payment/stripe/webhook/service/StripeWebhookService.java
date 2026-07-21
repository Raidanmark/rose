package com.rose.payment.stripe.webhook.service;

import com.rose.common.exception.stripe.InvalidStripeSignatureException;
import com.rose.donation.service.DonationService;
import com.rose.payment.account.service.UserPaymentAccountService;
import com.rose.payment.stripe.config.StripeProperties;
import com.rose.payment.stripe.connect.StripeConnectGatewayImpl;
import com.rose.payment.stripe.connect.StripeConnectedAccountSnapshot;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Account;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripeWebhookService {

    private final StripeProperties properties;
    private final UserPaymentAccountService paymentAccountService;
    private final DonationService donationService;
    private final StripeConnectGatewayImpl stripeConnectGateway;

    public void handle(String payload, String signature) {
        Event event = constructEvent(payload, signature);

        switch (event.getType()) {
            case "account.updated" ->
                    handleAccountUpdated(event);

            case "payment_intent.succeeded" ->
                    handlePaymentSucceeded(event);

            case "payment_intent.payment_failed" ->
                    handlePaymentFailed(event);

            case "payment_intent.canceled" ->
                    handlePaymentCanceled(event);

            default -> {
            }
        }
    }

    private Event constructEvent(
            String payload,
            String signature
    ) {
        try {
            return Webhook.constructEvent(
                    payload,
                    signature,
                    properties.webhookSecret()
            );
        } catch (SignatureVerificationException exception) {
            throw new InvalidStripeSignatureException(exception);
        }
    }

    private void handleAccountUpdated(Event event) {
        Account account = deserialize(event, Account.class);

        StripeConnectedAccountSnapshot snapshot =
                stripeConnectGateway.toSnapshot(account);

        paymentAccountService.synchronizeFromWebhook(
                snapshot.accountId(),
                snapshot.chargesEnabled(),
                snapshot.payoutsEnabled(),
                snapshot.disabled()
        );
    }

    private void handlePaymentSucceeded(Event event) {
        PaymentIntent paymentIntent =
                deserialize(event, PaymentIntent.class);

        donationService.markSucceeded(paymentIntent.getId());
    }

    private void handlePaymentFailed(Event event) {
        PaymentIntent paymentIntent =
                deserialize(event, PaymentIntent.class);

        donationService.markFailed(paymentIntent.getId());
    }

    private void handlePaymentCanceled(Event event) {
        PaymentIntent paymentIntent =
                deserialize(event, PaymentIntent.class);

        donationService.markCanceled(paymentIntent.getId());
    }

    private <T extends StripeObject> T deserialize(
            Event event,
            Class<T> expectedType
    ) {
        StripeObject stripeObject = event
                .getDataObjectDeserializer()
                .getObject()
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Could not deserialize Stripe event: "
                                        + event.getId()
                        )
                );

        if (!expectedType.isInstance(stripeObject)) {
            throw new IllegalStateException(
                    "Unexpected Stripe object type for event: "
                            + event.getType()
            );
        }

        return expectedType.cast(stripeObject);
    }
}
