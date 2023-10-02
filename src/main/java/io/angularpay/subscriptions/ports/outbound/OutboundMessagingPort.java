package io.angularpay.subscriptions.ports.outbound;

import java.util.Map;

public interface OutboundMessagingPort {
    Map<String, String> getPlatformConfigurations(String hashName);
}
