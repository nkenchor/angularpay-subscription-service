package io.angularpay.subscriptions.adapters.inbound;

import io.angularpay.subscriptions.configurations.AngularPayConfiguration;
import io.angularpay.subscriptions.domain.SubscriptionType;
import io.angularpay.subscriptions.domain.Subscriptions;
import io.angularpay.subscriptions.domain.commands.*;
import io.angularpay.subscriptions.models.*;
import io.angularpay.subscriptions.ports.inbound.RestApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static io.angularpay.subscriptions.helpers.Helper.fromHeaders;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class RestApiAdapter implements RestApiPort {

    private final CreateSubscriptionCommand createSubscriptionCommand;
    private final UpdateSubscriptionCommand updateSubscriptionCommand;
    private final DeleteSubscriptionCommand deleteSubscriptionCommand;
    private final GetSubscriptionByReferenceCommand getSubscriptionByReferenceCommand;
    private final GetSubscriptionListCommand getSubscriptionListCommand;
    private final GetSubscriptionListByTypeCommand getSubscriptionListByTypeCommand;
    private final GetStatisticsCommand getStatisticsCommand;

    private final AngularPayConfiguration configuration;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public GenericReferenceResponse createSubscription(
            @RequestBody GenericSubscriptionApiModel request,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        authenticatedUser.setUsername(request.getEmail());
        authenticatedUser.setUserReference(request.getEmail());
        CreateSubscriptionCommandRequest createSubscriptionCommandRequest = CreateSubscriptionCommandRequest.builder()
                .genericSubscriptionApiModel(request)
                .authenticatedUser(authenticatedUser)
                .build();
        return this.createSubscriptionCommand.execute(createSubscriptionCommandRequest);
    }

    @PutMapping("{subscriptionReference}")
    @Override
    public void updateSubscription(
            @PathVariable String subscriptionReference,
            @RequestBody GenericSubscriptionApiModel request,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        authenticatedUser.setUsername(request.getEmail());
        authenticatedUser.setUserReference(request.getEmail());
        UpdateSubscriptionCommandRequest updateSubscriptionCommandRequest = UpdateSubscriptionCommandRequest.builder()
                .reference(subscriptionReference)
                .genericSubscriptionApiModel(request)
                .authenticatedUser(authenticatedUser)
                .build();
        this.updateSubscriptionCommand.execute(updateSubscriptionCommandRequest);
    }

    @DeleteMapping("{subscriptionReference}")
    @Override
    public void deleteSubscription(
            @PathVariable String subscriptionReference,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GenericSubscriptionCommandRequest genericSubscriptionCommandRequest = GenericSubscriptionCommandRequest.builder()
                .reference(subscriptionReference)
                .authenticatedUser(authenticatedUser)
                .build();
        this.deleteSubscriptionCommand.execute(genericSubscriptionCommandRequest);
    }

    @GetMapping("{subscriptionReference}")
    @ResponseBody
    @Override
    public Subscriptions getSubscriptionByReference(
            @PathVariable String subscriptionReference,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GenericSubscriptionCommandRequest genericSubscriptionCommandRequest = GenericSubscriptionCommandRequest.builder()
                .reference(subscriptionReference)
                .authenticatedUser(authenticatedUser)
                .build();
        return this.getSubscriptionByReferenceCommand.execute(genericSubscriptionCommandRequest);
    }

    @GetMapping("/list/page/{page}")
    @ResponseBody
    @Override
    public List<Subscriptions> getSubscriptionList(
            @PathVariable int page,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetSubscriptionListCommandRequest getSubscriptionListCommandRequest = GetSubscriptionListCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .paging(Paging.builder().size(this.configuration.getPageSize()).index(page).build())
                .build();
        return this.getSubscriptionListCommand.execute(getSubscriptionListCommandRequest);
    }

    @GetMapping("/list/type/{type}/page/{page}")
    @ResponseBody
    @Override
    public List<Subscriptions> getSubscriptionByType(
            @PathVariable SubscriptionType type,
            @PathVariable int page,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetSubscriptionListByTypeCommandRequest getSubscriptionListByTypeCommandRequest = GetSubscriptionListByTypeCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .type(type)
                .paging(Paging.builder().size(this.configuration.getPageSize()).index(page).build())
                .build();
        return this.getSubscriptionListByTypeCommand.execute(getSubscriptionListByTypeCommandRequest);
    }

    @GetMapping("/statistics")
    @ResponseBody
    @Override
    public List<Statistics> getStatistics(@RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetStatisticsCommandRequest getStatisticsCommandRequest = GetStatisticsCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .build();
        return getStatisticsCommand.execute(getStatisticsCommandRequest);
    }
}
