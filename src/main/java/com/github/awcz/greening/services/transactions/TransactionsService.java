package com.github.awcz.greening.services.transactions;

import com.github.awcz.greening.generated.transactions.Account;
import com.github.awcz.greening.generated.transactions.Transaction;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.Entry.comparingByKey;

@Singleton
class TransactionsService {

    List<Account> processTransactions(List<Transaction> transactions) {
        Map<String, AccountSummary> accounts = new HashMap<>();
        transactions.forEach(transaction -> processTransaction(transaction, accounts));
        return buildAccountsSummary(accounts);
    }

    private List<Account> buildAccountsSummary(Map<String, AccountSummary> balances) {
        return balances.entrySet().stream()
                .sorted(comparingByKey())
                .map(summary -> toAccount(summary.getKey(), summary.getValue()))
                .toList();
    }

    private Account toAccount(String accountNumber, AccountSummary accountSummary) {
        return new CustomAccount()
                .account(accountNumber)
                .balance(accountSummary.balance.floatValue())
                .debitCount(accountSummary.debits)
                .creditCount(accountSummary.credits);
    }

    private void processTransaction(Transaction transaction, Map<String, AccountSummary> balances) {
        var creditSummary = balances.computeIfAbsent(transaction.getCreditAccount(), c -> new AccountSummary());
        var debitSummary = balances.computeIfAbsent(transaction.getDebitAccount(), c -> new AccountSummary());

        creditSummary.balance = creditSummary.balance.add(BigDecimal.valueOf(transaction.getAmount()));
        creditSummary.credits++;
        debitSummary.balance = debitSummary.balance.subtract(BigDecimal.valueOf(transaction.getAmount()));
        debitSummary.debits++;
    }

    private static class AccountSummary {
        private BigDecimal balance = BigDecimal.ZERO;
        private int credits;
        private int debits;
    }
}
