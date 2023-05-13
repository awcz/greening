package com.github.awcz.greening.services.transactions;

import com.github.awcz.greening.generated.transactions.Account;
import com.github.awcz.greening.generated.transactions.Transaction;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import javax.validation.Valid;
import java.util.List;

@Controller
@ExecuteOn(TaskExecutors.IO)
class TransactionsReportController {

    private final TransactionsService transactionsService;

    TransactionsReportController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    /**
     * Generates a report for debit and credit transactions.
     *
     * @param transactions : list of transactions
     * @return : ordered list of accounts summaries
     */
    @Post("/transactions/report")
    List<Account> report(@Body @Valid List<Transaction> transactions) {
        TransactionRequestValidator.validate(transactions);
        return transactionsService.processTransactions(transactions);
    }
}
