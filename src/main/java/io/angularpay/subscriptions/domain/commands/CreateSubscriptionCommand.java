package io.angularpay.subscriptions.domain.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.angularpay.subscriptions.adapters.outbound.GoogleReCaptchaV3Adapter;
import io.angularpay.subscriptions.adapters.outbound.MongoAdapter;
import io.angularpay.subscriptions.configurations.AngularPayConfiguration;
import io.angularpay.subscriptions.domain.Role;
import io.angularpay.subscriptions.domain.Subscriptions;
import io.angularpay.subscriptions.exceptions.CommandException;
import io.angularpay.subscriptions.exceptions.ErrorObject;
import io.angularpay.subscriptions.models.CreateSubscriptionCommandRequest;
import io.angularpay.subscriptions.models.GoogleReCaptchaRequest;
import io.angularpay.subscriptions.models.GoogleReCaptchaResponse;
import io.angularpay.subscriptions.models.ResourceReferenceResponse;
import io.angularpay.subscriptions.ports.outbound.GoogleReCaptchaV3Port;
import io.angularpay.subscriptions.validation.DefaultConstraintValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.angularpay.subscriptions.exceptions.ErrorCode.AUTHORIZATION_ERROR;
import static io.angularpay.subscriptions.helpers.CommandHelper.validateNotExistOrThrow;
import static io.angularpay.subscriptions.helpers.Helper.maskEmail;

@Slf4j
@Service
public class CreateSubscriptionCommand extends AbstractCommand<CreateSubscriptionCommandRequest, ResourceReferenceResponse>
        implements SensitiveDataCommand<CreateSubscriptionCommandRequest> {

    private final DefaultConstraintValidator validator;
    private final GoogleReCaptchaV3Port googleReCaptchaV3Port;
    private final AngularPayConfiguration configuration;
    private final MongoAdapter mongoAdapter;

    public CreateSubscriptionCommand(
            ObjectMapper mapper,
            DefaultConstraintValidator validator,
            GoogleReCaptchaV3Adapter googleReCaptchaV3Port,
            AngularPayConfiguration configuration,
            MongoAdapter mongoAdapter) {
        super("CreateSubscriptionCommand", mapper);
        this.validator = validator;
        this.googleReCaptchaV3Port = googleReCaptchaV3Port;
        this.configuration = configuration;
        this.mongoAdapter = mongoAdapter;
    }

    @Override
    protected String getResourceOwner(CreateSubscriptionCommandRequest request) {
        return request.getGenericSubscriptionApiModel().getEmail();
    }

    @Override
    protected ResourceReferenceResponse handle(CreateSubscriptionCommandRequest request) {
        validateNotExistOrThrow(this.mongoAdapter, request.getGenericSubscriptionApiModel().getEmail());

        boolean success = !this.configuration.getGoogleRecaptcha().isEnabled() || isRecaptchaSuccess(request.getGenericSubscriptionApiModel().getRecaptcha());
        if (!success) {
            throw CommandException.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .errorCode(AUTHORIZATION_ERROR)
                    .message(AUTHORIZATION_ERROR.getDefaultMessage())
                    .build();
        }

        Subscriptions subscription = Subscriptions.builder()
                .reference(UUID.randomUUID().toString())
                .subscriberName(request.getGenericSubscriptionApiModel().getSubscriberName())
                .email(request.getGenericSubscriptionApiModel().getEmail())
                .subscriptions(request.getGenericSubscriptionApiModel().getSubscriptions())
                .build();

        Subscriptions response = mongoAdapter.createSubscription(subscription);

        return new ResourceReferenceResponse(response.getReference());
    }

    private boolean isRecaptchaSuccess(GoogleReCaptchaRequest reCaptchaRequest) {
        Optional<GoogleReCaptchaResponse> optional = this.googleReCaptchaV3Port.recapatcha(GoogleReCaptchaRequest.builder()
                .recaptchaToken(reCaptchaRequest.getRecaptchaToken())
                .actionName(reCaptchaRequest.getActionName())
                .build());

        if (optional.isEmpty()) {
            throw CommandException.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .errorCode(AUTHORIZATION_ERROR)
                    .message(AUTHORIZATION_ERROR.getDefaultMessage())
                    .build();
        }

        GoogleReCaptchaResponse googleReCaptchaResponse = optional.get();

        String captchaRequestActionName = reCaptchaRequest.getActionName();
        float expectedScore = configuration.getGoogleRecaptcha().getThreshold();
        String captchaResponseActionName = googleReCaptchaResponse.getAction();
        float scoreFromResponse = googleReCaptchaResponse.getScore();
        boolean verificationResultOk = googleReCaptchaResponse.isSuccess();

        return captchaResponseActionName.equals(captchaRequestActionName) && verificationResultOk && scoreFromResponse > expectedScore;
    }

    @Override
    protected List<ErrorObject> validate(CreateSubscriptionCommandRequest request) {
        return this.validator.validate(request);
    }

    @Override
    protected List<Role> permittedRoles() {
        return Collections.emptyList();
    }

    @Override
    public CreateSubscriptionCommandRequest mask(CreateSubscriptionCommandRequest raw) {
        try {
            JsonNode node = mapper.convertValue(raw, JsonNode.class);
            JsonNode genericSubscriptionApiModel = node.get("genericSubscriptionApiModel");
            ((ObjectNode) genericSubscriptionApiModel).put("email", maskEmail(raw.getGenericSubscriptionApiModel().getEmail()));
            return mapper.treeToValue(node, CreateSubscriptionCommandRequest.class);
        } catch (JsonProcessingException exception) {
            return raw;
        }
    }
}
