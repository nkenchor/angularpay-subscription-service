package io.angularpay.subscriptions.ports.outbound;

import io.angularpay.subscriptions.domain.SubscriptionType;
import io.angularpay.subscriptions.domain.Subscriptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PersistencePort {
    Subscriptions createSubscription(Subscriptions subscriptions);
    Subscriptions updateSubscription(Subscriptions subscriptions);
    void deleteSubscription(Subscriptions subscriptions);
    Optional<Subscriptions> findSubscriptionByReference(String reference);
    Optional<Subscriptions> findSubscriptionByEmail(String email);
    Page<Subscriptions> listSubscriptions(Pageable pageable);
    Page<Subscriptions> listSubscriptionsByType(SubscriptionType type, Pageable pageable);
    long getCountBySubscriptionType(SubscriptionType type);
    long getTotalCount();
}
