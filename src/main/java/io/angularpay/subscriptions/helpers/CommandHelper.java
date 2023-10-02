package io.angularpay.subscriptions.helpers;

import io.angularpay.subscriptions.adapters.outbound.MongoAdapter;
import io.angularpay.subscriptions.domain.Subscriptions;
import io.angularpay.subscriptions.exceptions.CommandException;
import io.angularpay.subscriptions.exceptions.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static io.angularpay.subscriptions.exceptions.ErrorCode.DUPLICATE_REQUEST_ERROR;
import static io.angularpay.subscriptions.exceptions.ErrorCode.REQUEST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommandHelper {

    private final MongoAdapter mongoAdapter;

    public String getResourceOwner(String requestReference) {
        Subscriptions found = this.mongoAdapter.findSubscriptionByReference(requestReference).orElseThrow(
                () -> commandException(HttpStatus.NOT_FOUND, REQUEST_NOT_FOUND)
        );
        return found.getEmail();
    }

    public static Subscriptions getSubscriptionsByReferenceOrThrow(MongoAdapter mongoAdapter, String reference) {
        return mongoAdapter.findSubscriptionByReference(reference).orElseThrow(
                () -> commandException(HttpStatus.NOT_FOUND, REQUEST_NOT_FOUND)
        );
    }

    public static Subscriptions getSubscriptionsByEmailOrThrow(MongoAdapter mongoAdapter, String email) {
        return mongoAdapter.findSubscriptionByEmail(email).orElseThrow(
                () -> commandException(HttpStatus.UNAUTHORIZED, REQUEST_NOT_FOUND)
        );
    }

    public static void validateNotExistOrThrow(MongoAdapter mongoAdapter, String email) {
        mongoAdapter.findSubscriptionByEmail(email).ifPresent(
                (x) -> {
                    throw commandException(HttpStatus.CONFLICT, DUPLICATE_REQUEST_ERROR);
                }
        );
    }

    private static CommandException commandException(HttpStatus status, ErrorCode errorCode) {
        return CommandException.builder()
                .status(status)
                .errorCode(errorCode)
                .message(errorCode.getDefaultMessage())
                .build();
    }

}
