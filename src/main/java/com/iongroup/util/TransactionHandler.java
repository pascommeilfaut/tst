package com.iongroup.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TransactionHandler {

    @Transactional
    public <T> T runInTransaction(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional(readOnly = true)
    public <T> T runInTransactionReadOnly(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional
    public void runInTransaction(Runnable runnable) {
        runnable.run();
    }

    @Transactional(readOnly = true)
    public void runInTransactionReadOnly(Runnable runnable) {
        runnable.run();
    }

}
