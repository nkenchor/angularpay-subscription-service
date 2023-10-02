package io.angularpay.subscriptions.adapters.outbound;

import io.angularpay.subscriptions.domain.SubscriptionType;
import io.angularpay.subscriptions.domain.Subscriptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<Subscriptions, String> {

    Optional<Subscriptions> findByReference(String reference);
    Optional<Subscriptions> findByEmail(String email);
    Page<Subscriptions> findBySubscriptionsContains(SubscriptionType type, Pageable pageable);
    long countBySubscriptionsContains(SubscriptionType type);
}
