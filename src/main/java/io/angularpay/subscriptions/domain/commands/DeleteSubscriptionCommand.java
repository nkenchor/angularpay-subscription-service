package io.angularpay.subscriptions.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.subscriptions.adapters.outbound.MongoAdapter;
import io.angularpay.subscriptions.domain.Role;
import io.angularpay.subscriptions.domain.Subscriptions;
import io.angularpay.subscriptions.exceptions.ErrorObject;
import io.angularpay.subscriptions.helpers.CommandHelper;
import io.angularpay.subscriptions.models.GenericSubscriptionCommandRequest;
import io.angularpay.subscriptions.validation.DefaultConstraintValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static io.angularpay.subscriptions.helpers.CommandHelper.getSubscriptionsByReferenceOrThrow;

@Slf4j
@Service
public class DeleteSubscriptionCommand extends AbstractCommand<GenericSubscriptionCommandRequest, Void> {

    private final DefaultConstraintValidator validator;
    private final MongoAdapter mongoAdapter;
    private final CommandHelper commandHelper;

    public DeleteSubscriptionCommand(
            ObjectMapper mapper,
            DefaultConstraintValidator validator,
            MongoAdapter mongoAdapter,
            CommandHelper commandHelper) {
        super("DeleteSubscriptionCommand", mapper);
        this.validator = validator;
        this.mongoAdapter = mongoAdapter;
        this.commandHelper = commandHelper;
    }

    @Override
    protected String getResourceOwner(GenericSubscriptionCommandRequest request) {
        return this.commandHelper.getResourceOwner(request.getReference());
    }

    @Override
    protected Void handle(GenericSubscriptionCommandRequest request) {
        Subscriptions subscription = getSubscriptionsByReferenceOrThrow(this.mongoAdapter, request.getReference());
        this.mongoAdapter.deleteSubscription(subscription);
        return null;
    }

    @Override
    protected List<ErrorObject> validate(GenericSubscriptionCommandRequest request) {
        return this.validator.validate(request);
    }

    @Override
    protected List<Role> permittedRoles() {
        return Arrays.asList(Role.ROLE_SUBSCRIPTION_ADMIN, Role.ROLE_PLATFORM_ADMIN);
    }

}
