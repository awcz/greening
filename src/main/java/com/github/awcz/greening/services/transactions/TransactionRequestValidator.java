package com.github.awcz.greening.services.transactions;

import com.github.awcz.greening.errors.RequestValidationException;
import com.github.awcz.greening.generated.transactions.Transaction;

import java.util.List;

import static java.util.Objects.isNull;

class TransactionRequestValidator {

    static final int MAX_TRANSACTIONS_NUMBER = 100_000;

    private TransactionRequestValidator() {
    }

    static void validate(List<Transaction> tasks) {
        if (tasks.size() > MAX_TRANSACTIONS_NUMBER) {
            throw new RequestValidationException("The number of transactions exceeds the maximum allowed size: " + MAX_TRANSACTIONS_NUMBER);
        }
        if (tasks.stream().anyMatch(task -> (isNull(task.getAmount()) || isNull(task.getCreditAccount()) || isNull(task.getDebitAccount())))) {
            throw new RequestValidationException("'amount', 'creditAccount' and 'debitAccount'  cannot be null");
        }
    }
}
