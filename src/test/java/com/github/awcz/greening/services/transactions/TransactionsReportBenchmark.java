package com.github.awcz.greening.services.transactions;

import com.github.awcz.greening.benchmarks.AbstractBenchmarkRunner;
import com.github.awcz.greening.benchmarks.AbstractHttpBenchmark;
import com.github.awcz.greening.generated.transactions.Transaction;
import io.micronaut.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransactionsReportBenchmark extends AbstractBenchmarkRunner {

    @Test
    void runBenchmark() throws Exception {
        this.launchBenchmark(HttpBenchmark.class);
    }

    public static class HttpBenchmark extends AbstractHttpBenchmark {

        private final TransactionsReportRequestGenerator requestGenerator = new TransactionsReportRequestGenerator();

        @Param({"10000", "100000"})
        int transactionsCount;

        private List<Transaction> testData;

        @Setup(Level.Invocation)
        public void prepare() {
            testData = requestGenerator.generateRequest(transactionsCount);
        }

        @Benchmark
        public void generateReport() {
            var request = HttpRequest.POST("/transactions/report", testData);
            var body = client.toBlocking().retrieve(request);
            assertNotNull(body);
        }
    }
}