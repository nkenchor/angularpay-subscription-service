package io.angularpay.subscriptions.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("angularpay")
@Data
public class AngularPayConfiguration {

    private int pageSize;
    private GoogleRecaptcha googleRecaptcha;
    private Redis redis;

    @Data
    public static class Redis {
        private String host;
        private int port;
        private int timeout;
    }

    @Data
    public static class GoogleRecaptcha {
        private String url;
        private String key;
        private String secret;
        private float threshold;
        private boolean enabled;
    }
}
