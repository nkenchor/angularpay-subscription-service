package io.angularpay.subscriptions.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GenericSubscriptionCommandRequest extends AccessControl {

    @NotEmpty
    private String reference;

    GenericSubscriptionCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
