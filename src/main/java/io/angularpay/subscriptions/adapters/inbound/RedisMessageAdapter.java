package io.angularpay.subscriptions.adapters.inbound;

import io.angularpay.subscriptions.domain.commands.PlatformConfigurationsConverterCommand;
import io.angularpay.subscriptions.models.platform.PlatformConfigurationIdentifier;
import io.angularpay.subscriptions.ports.inbound.InboundMessagingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static io.angularpay.subscriptions.models.platform.PlatformConfigurationSource.TOPIC;

@Service
@RequiredArgsConstructor
public class RedisMessageAdapter implements InboundMessagingPort {

    private final PlatformConfigurationsConverterCommand converterCommand;

    @Override
    public void onMessage(String message, PlatformConfigurationIdentifier identifier) {
        this.converterCommand.execute(message, identifier, TOPIC);
    }
}
