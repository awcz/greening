package com.github.awcz.greening.benchmarks;

import io.micronaut.http.client.DefaultHttpClientConfiguration;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.netty.DefaultHttpClient;
import org.openjdk.jmh.annotations.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(value = 1)
@BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(org.openjdk.jmh.annotations.Scope.Benchmark)
public abstract class AbstractHttpBenchmark {

    protected HttpClient client;

    @Setup
    public void setup() throws URISyntaxException {
        String testPort = System.getProperty("jmhTestPort");
        DefaultHttpClientConfiguration configuration = new DefaultHttpClientConfiguration();
        configuration.setReadTimeout(Duration.of(30, ChronoUnit.SECONDS));
        configuration.setConnectTimeout(Duration.of(30, ChronoUnit.SECONDS));
        this.client = new DefaultHttpClient(new URI("http://127.0.0.1:" + testPort), configuration);
    }

    @TearDown
    public void tearDown() {
        this.client.close();
    }
}