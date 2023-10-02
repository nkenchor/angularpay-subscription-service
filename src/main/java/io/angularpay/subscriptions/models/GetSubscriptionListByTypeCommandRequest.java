package io.angularpay.subscriptions.models;

import io.angularpay.subscriptions.domain.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GetSubscriptionListByTypeCommandRequest extends AccessControl {

    @NotNull
    private SubscriptionType type;

    @NotNull
    @Valid
    private Paging paging;

    GetSubscriptionListByTypeCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
