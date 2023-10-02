package io.angularpay.subscriptions.ports.outbound;

import io.angularpay.subscriptions.models.GoogleReCaptchaRequest;
import io.angularpay.subscriptions.models.GoogleReCaptchaResponse;

import java.util.Optional;

public interface GoogleReCaptchaV3Port {
    Optional<GoogleReCaptchaResponse> recapatcha(GoogleReCaptchaRequest request);
}
