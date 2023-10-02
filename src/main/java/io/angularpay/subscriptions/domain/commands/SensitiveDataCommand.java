package io.angularpay.subscriptions.domain.commands;

public interface SensitiveDataCommand<T> {
    T mask(T raw);
}
