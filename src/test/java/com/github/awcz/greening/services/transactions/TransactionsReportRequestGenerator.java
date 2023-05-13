package com.github.awcz.greening.services.transactions;

import com.github.awcz.greening.generated.transactions.Transaction;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.github.awcz.greening.utils.RandomGeneratorProvider.getRandomGenerator;

public class TransactionsReportRequestGenerator {

    private static final Random RANDOM = getRandomGenerator();

    public List<Transaction> generateRequest(int transactionsCount) {
        return IntStream.range(0, transactionsCount)
                .mapToObj(value -> buildTransaction()).toList();
    }

    private Transaction buildTransaction() {
        return new Transaction().debitAccount(getRandomAccountNumber())
                .creditAccount(getRandomAccountNumber())
                .amount(RANDOM.nextFloat() * 100);
    }

    private String getRandomAccountNumber() {
        int maxAccountNumber = 600;
        var acc = String.valueOf((int) (RANDOM.nextDouble() * maxAccountNumber));
        return "0".repeat(26 - acc.length()) + acc;
    }
}
