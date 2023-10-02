package io.angularpay.subscriptions.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UpdateSubscriptionCommandRequest extends AccessControl {

    @NotEmpty
    private String reference;

    @NotNull
    @Valid
    private GenericSubscriptionApiModel genericSubscriptionApiModel;

    UpdateSubscriptionCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
