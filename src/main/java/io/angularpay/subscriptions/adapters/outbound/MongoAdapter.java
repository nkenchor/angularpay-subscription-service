package io.angularpay.subscriptions.adapters.outbound;

import io.angularpay.subscriptions.domain.SubscriptionType;
import io.angularpay.subscriptions.domain.Subscriptions;
import io.angularpay.subscriptions.ports.outbound.PersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MongoAdapter implements PersistencePort {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Subscriptions createSubscription(Subscriptions subscriptions) {
        subscriptions.setCreatedOn(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        subscriptions.setLastModified(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        return subscriptionRepository.save(subscriptions);
    }

    @Override
    public Subscriptions updateSubscription(Subscriptions subscriptions) {
        subscriptions.setLastModified(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        return subscriptionRepository.save(subscriptions);
    }

    @Override
    public void deleteSubscription(Subscriptions subscriptions) {
        this.subscriptionRepository.delete(subscriptions);
    }

    @Override
    public Optional<Subscriptions> findSubscriptionByReference(String reference) {
        return subscriptionRepository.findByReference(reference);
    }

    @Override
    public Optional<Subscriptions> findSubscriptionByEmail(String email) {
        return subscriptionRepository.findByEmail(email);
    }

    @Override
    public Page<Subscriptions> listSubscriptions(Pageable pageable) {
        return this.subscriptionRepository.findAll(pageable);
    }

    @Override
    public Page<Subscriptions> listSubscriptionsByType(SubscriptionType type, Pageable pageable) {
        return this.subscriptionRepository.findBySubscriptionsContains(type, pageable);
    }

    @Override
    public long getCountBySubscriptionType(SubscriptionType type) {
        return subscriptionRepository.countBySubscriptionsContains(type);
    }

    @Override
    public long getTotalCount() {
        return subscriptionRepository.count();
    }
}
