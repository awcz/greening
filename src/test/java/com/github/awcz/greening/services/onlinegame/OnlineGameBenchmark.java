package com.github.awcz.greening.services.onlinegame;

import com.github.awcz.greening.benchmarks.AbstractBenchmarkRunner;
import com.github.awcz.greening.benchmarks.AbstractHttpBenchmark;
import com.github.awcz.greening.generated.onlinegame.Players;
import io.micronaut.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OnlineGameBenchmark extends AbstractBenchmarkRunner {

    @Test
    void runBenchmark() throws Exception {
        this.launchBenchmark(HttpBenchmark.class);
    }

    public static class HttpBenchmark extends AbstractHttpBenchmark {
        private final OnlineGameRequestGenerator onlineGameRequestGenerator = new OnlineGameRequestGenerator();

        @Param({"10", "1000"})
        int groupCount;
        @Param({"500", "20000"})
        int clans;

        private Players testData;

        @Setup(Level.Invocation)
        public void prepare() {
            testData = onlineGameRequestGenerator.generateRequest(clans, groupCount);
        }

        @Benchmark
        public void calculateClans() {
            var request = HttpRequest.POST("/onlinegame/calculate", testData);
            var body = client.toBlocking().retrieve(request);
            assertNotNull(body);
        }
    }
}