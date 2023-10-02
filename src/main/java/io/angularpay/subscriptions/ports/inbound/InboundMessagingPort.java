package io.angularpay.subscriptions.ports.inbound;

import io.angularpay.subscriptions.models.platform.PlatformConfigurationIdentifier;

public interface InboundMessagingPort {
    void onMessage(String message, PlatformConfigurationIdentifier identifier);
}
