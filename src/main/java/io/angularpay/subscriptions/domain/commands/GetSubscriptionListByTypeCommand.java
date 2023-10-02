package io.angularpay.subscriptions.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.subscriptions.adapters.outbound.MongoAdapter;
import io.angularpay.subscriptions.domain.Role;
import io.angularpay.subscriptions.domain.Subscriptions;
import io.angularpay.subscriptions.exceptions.ErrorObject;
import io.angularpay.subscriptions.models.GetSubscriptionListByTypeCommandRequest;
import io.angularpay.subscriptions.validation.DefaultConstraintValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class GetSubscriptionListByTypeCommand extends AbstractCommand<GetSubscriptionListByTypeCommandRequest, List<Subscriptions>> {

    private final MongoAdapter mongoAdapter;
    private final DefaultConstraintValidator validator;

    public GetSubscriptionListByTypeCommand(ObjectMapper mapper, MongoAdapter mongoAdapter, DefaultConstraintValidator validator) {
        super("GetSubscriptionListByTypeCommand", mapper);
        this.mongoAdapter = mongoAdapter;
        this.validator = validator;
    }

    @Override
    protected String getResourceOwner(GetSubscriptionListByTypeCommandRequest request) {
        return "";
    }

    @Override
    protected List<Subscriptions> handle(GetSubscriptionListByTypeCommandRequest request) {
        Pageable pageable = PageRequest.of(request.getPaging().getIndex(), request.getPaging().getSize());
        return this.mongoAdapter.listSubscriptionsByType(request.getType(), pageable).getContent();
    }

    @Override
    protected List<ErrorObject> validate(GetSubscriptionListByTypeCommandRequest request) {
        return this.validator.validate(request);
    }

    @Override
    protected List<Role> permittedRoles() {
        return Arrays.asList(Role.ROLE_SUBSCRIPTION_ADMIN, Role.ROLE_PLATFORM_ADMIN);
    }
}
