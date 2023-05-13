package com.github.awcz.greening.services.atmservice;

import com.github.awcz.greening.benchmarks.AbstractBenchmarkRunner;
import com.github.awcz.greening.benchmarks.AbstractHttpBenchmark;
import com.github.awcz.greening.generated.atmservice.Task;
import io.micronaut.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AtmServiceBenchmark extends AbstractBenchmarkRunner {

    @Test
    void runBenchmark() throws Exception {
        this.launchBenchmark(HttpBenchmark.class);
    }

    public static class HttpBenchmark extends AbstractHttpBenchmark {

        @Param({"20000", "100000"})
        int tasks;

        private List<Task> testData;

        @Setup(Level.Invocation)
        public void prepare() {
            testData = new AtmRequestGenerator().generateRequest(tasks);
        }

        @Benchmark
        public void calculateOrder() {
            var request = HttpRequest.POST("/atms/calculateOrder", testData);
            var body = client.toBlocking().retrieve(request);
            assertNotNull(body);
        }
    }
}