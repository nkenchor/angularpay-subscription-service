package io.angularpay.subscriptions.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.subscriptions.adapters.outbound.MongoAdapter;
import io.angularpay.subscriptions.domain.SubscriptionType;
import io.angularpay.subscriptions.domain.Role;
import io.angularpay.subscriptions.exceptions.ErrorObject;
import io.angularpay.subscriptions.models.GetStatisticsCommandRequest;
import io.angularpay.subscriptions.models.Statistics;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class GetStatisticsCommand extends AbstractCommand<GetStatisticsCommandRequest, List<Statistics>> {

    private final MongoAdapter mongoAdapter;

    public GetStatisticsCommand(ObjectMapper mapper, MongoAdapter mongoAdapter) {
        super("GetStatisticsCommand", mapper);
        this.mongoAdapter = mongoAdapter;
    }

    @Override
    protected String getResourceOwner(GetStatisticsCommandRequest request) {
        return "";
    }

    @Override
    protected List<Statistics> handle(GetStatisticsCommandRequest request) {
        List<Statistics> statistics = new ArrayList<>();

        long total = this.mongoAdapter.getTotalCount();
        statistics.add(Statistics.builder()
                .name("Total")
                .value(String.valueOf(total))
                .build());

        long blogPosts = this.mongoAdapter.getCountBySubscriptionType(SubscriptionType.BLOGPOSTS);
        statistics.add(Statistics.builder()
                .name("Blog Posts")
                .value(String.valueOf(blogPosts))
                .build());

        long earlyAccess = this.mongoAdapter.getCountBySubscriptionType(SubscriptionType.MVP_EARLY_ACCESS);
        statistics.add(Statistics.builder()
                .name("Early Access")
                .value(String.valueOf(earlyAccess))
                .build());

        long newsletters = this.mongoAdapter.getCountBySubscriptionType(SubscriptionType.BLOGPOSTS);
        statistics.add(Statistics.builder()
                .name("Newsletters")
                .value(String.valueOf(newsletters))
                .build());

        return statistics;
    }

    @Override
    protected List<ErrorObject> validate(GetStatisticsCommandRequest request) {
        return Collections.emptyList();
    }

    @Override
    protected List<Role> permittedRoles() {
        return Arrays.asList(Role.ROLE_PLATFORM_ADMIN, Role.ROLE_PLATFORM_USER);
    }
}
