package io.angularpay.subscriptions.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.angularpay.subscriptions.domain.SubscriptionType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

import static io.angularpay.subscriptions.common.Constants.REGEX_EMAIL_ADDRESS;

@Data
public class GenericSubscriptionApiModel {

    @NotEmpty
    @JsonProperty("subscriber_name")
    private String subscriberName;

    @Pattern(regexp = REGEX_EMAIL_ADDRESS, message = "Invalid email address", flags = {Pattern.Flag.CASE_INSENSITIVE})
    private String email;

    @NotNull
    @NotEmpty
    private List<SubscriptionType> subscriptions;

    @NotNull
    @Valid
    private GoogleReCaptchaRequest recaptcha;
}
