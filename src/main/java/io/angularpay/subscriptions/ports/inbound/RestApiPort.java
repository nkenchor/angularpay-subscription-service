package io.angularpay.subscriptions.ports.inbound;

import io.angularpay.subscriptions.domain.SubscriptionType;
import io.angularpay.subscriptions.domain.Subscriptions;
import io.angularpay.subscriptions.models.GenericReferenceResponse;
import io.angularpay.subscriptions.models.GenericSubscriptionApiModel;
import io.angularpay.subscriptions.models.Statistics;

import java.util.List;
import java.util.Map;

public interface RestApiPort {
    GenericReferenceResponse createSubscription(GenericSubscriptionApiModel request, Map<String, String> headers);
    void updateSubscription(String subscriptionReference, GenericSubscriptionApiModel request, Map<String, String> headers);
    void deleteSubscription(String subscriptionReference, Map<String, String> headers);
    Subscriptions getSubscriptionByReference(String subscriptionReference, Map<String, String> headers);
    List<Subscriptions> getSubscriptionList(int page, Map<String, String> headers);
    List<Subscriptions> getSubscriptionByType(SubscriptionType type, int page, Map<String, String> headers);
    List<Statistics> getStatistics(Map<String, String> headers);
}
