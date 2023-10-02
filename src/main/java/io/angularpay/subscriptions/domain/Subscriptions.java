
package io.angularpay.subscriptions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document("subscriptions")
public class Subscriptions {

    @Id
    private String id;
    @Version
    private int version;
    private String reference;
    @JsonProperty("created_on")
    private String createdOn;
    @JsonProperty("last_modified")
    private String lastModified;
    @JsonProperty("subscriber_name")
    private String subscriberName;
    private String email;
    private List<SubscriptionType> subscriptions;
}
