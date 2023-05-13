package com.github.awcz.greening.benchmarks;

import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MicronautTest
public abstract class AbstractBenchmarkRunner {

    @Inject
    EmbeddedServer server;

    protected void launchBenchmark(Class<? extends AbstractHttpBenchmark> benchmarkClass) throws Exception {
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy__HH_mm_ss"));
        String className = benchmarkClass.getCanonicalName();
        new Runner(new OptionsBuilder()
                .jvmArgsAppend("-DjmhTestPort=" + server.getPort())
                .resultFormat(ResultFormatType.JSON)
                .result("target/benchmark_results_" + className.replace(".", "_") + "_" + datetime + ".json")
                .include(className)
                .build())
                .run();
    }
}
