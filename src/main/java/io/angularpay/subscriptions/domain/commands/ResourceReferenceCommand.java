package io.angularpay.subscriptions.domain.commands;

public interface ResourceReferenceCommand<T, R> {

    R map(T referenceResponse);
}
