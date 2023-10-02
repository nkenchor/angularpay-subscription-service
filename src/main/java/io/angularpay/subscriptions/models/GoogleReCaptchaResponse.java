package io.angularpay.subscriptions.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GoogleReCaptchaResponse {

    private String action;
    @JsonProperty("challenge_ts")
    private String challengeTs;
    @JsonProperty("error-codes")
    private List<String> errorCodes;
    private String hostname;
    private float score;
    private boolean success;
}
