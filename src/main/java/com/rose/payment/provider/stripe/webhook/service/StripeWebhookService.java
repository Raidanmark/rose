package com.rose.payment.provider.stripe.webhook.service;

import com.rose.donation.service.DonationService;
import com.rose.payment.provider.stripe.config.StripeProperties;
import com.rose.payment.provider.stripe.exception.InvalidStripeSignatureException;
import com.rose.payment.provider.stripe.webhook.repository.StripeWebhookEventRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StripeWebhookService {

    private final StripeProperties properties;
    private final DonationService donationService;
    private final StripeWebhookEventRepository stripeWebhookEventRepository;

    @Transactional
    public void handle(String payload, String signature) {
        Event event = constructEvent(payload, signature);

        if (!isSupportedEvent(event.getType())) {
            return;
        }

        int claimed = stripeWebhookEventRepository.claimEvent(
                event.getId(),
                event.getType()
        );

        if (claimed == 0) {
            return;
        }

        switch (event.getType()) {

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

    private boolean isSupportedEvent(
            String eventType
    ) {
        return switch (eventType) {
            case "account.updated",
                 "payment_intent.succeeded",
                 "payment_intent.payment_failed",
                 "payment_intent.canceled" -> true;

            default -> false;
        };
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
