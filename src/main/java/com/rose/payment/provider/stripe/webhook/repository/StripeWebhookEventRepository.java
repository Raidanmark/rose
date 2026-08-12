package com.rose.payment.provider.stripe.webhook.repository;

import com.rose.payment.provider.stripe.webhook.entity.StripeWebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StripeWebhookEventRepository extends JpaRepository<StripeWebhookEvent, String> {
    @Modifying
    @Query(
            value = """
                    INSERT INTO stripe_webhook_events (
                        id,
                        event_type,
                        processed_at
                    )
                    VALUES (
                        :id,
                        :eventType,
                        CURRENT_TIMESTAMP
                    )
                    ON CONFLICT (id) DO NOTHING
                    """,
            nativeQuery = true
    )
    int claimEvent(
            @Param("id") String eventId,
            @Param("eventType") String eventType
    );
}
