package com.github.awcz.greening.services.transactions;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class TransactionsReportControllerIntegrationTest {

    private final TransactionsReportRequestGenerator requestGenerator = new TransactionsReportRequestGenerator();

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void shouldReturnExpectedResult() {
        // given
        var input = """
                [
                  {
                    "debitAccount": "32309111922661937852684864",
                    "creditAccount": "06105023389842834748547303",
                    "amount": 10.90
                  },
                  {
                    "debitAccount": "31074318698137062235845814",
                    "creditAccount": "66105036543749403346524547",
                    "amount": 200.90
                  },
                  {
                    "debitAccount": "66105036543749403346524547",
                    "creditAccount": "32309111922661937852684864",
                    "amount": 50.10
                  }
                ]
                """;
        var request = HttpRequest.POST("/transactions/report", input);

        // when
        var body = client.toBlocking().retrieve(request);

        // then
        assertNotNull(body);
        assertEquals("""
                [
                  {
                    "account": "06105023389842834748547303",
                    "debitCount": 0,
                    "creditCount": 1,
                    "balance": 10.90
                  },
                  {
                    "account": "31074318698137062235845814",
                    "debitCount": 1,
                    "creditCount": 0,
                    "balance": -200.90
                  },
                  {
                    "account": "32309111922661937852684864",
                    "debitCount": 1,
                    "creditCount": 1,
                    "balance": 39.20
                  },
                  {
                    "account": "66105036543749403346524547",
                    "debitCount": 1,
                    "creditCount": 1,
                    "balance": 150.80
                  }
                ]

                """.replace("\n", "").replace(" ", ""), body);
    }

    @Test
    void shouldReturnNonEmptyResponse() {
        // given
        var transactions = requestGenerator.generateRequest(100_000);
        var httpRequest = HttpRequest.POST("/transactions/report", transactions);

        // when
        var response = client.toBlocking().retrieve(httpRequest, List.class);

        // then
        assertFalse(response.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenTransactionsListIsTooLong() {
        // given
        var transactions = requestGenerator.generateRequest(100_000 + 1);
        var httpRequest = HttpRequest.POST("/transactions/report", transactions);
        var httpClient = client.toBlocking();

        // when - then
        var responseException = assertThrows(
                HttpClientResponseException.class,
                () -> {
                    httpClient.retrieve(httpRequest);
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, responseException.getStatus());
    }

    @ParameterizedTest
    @MethodSource("invalidTestCases")
    void shouldReturnHttp400WhenRequestIsInvalid(String invalidInput) {
        // given
        var request = HttpRequest.POST("/transactions/report", invalidInput);
        var httpClient = client.toBlocking();

        // when - then
        var responseException = assertThrows(HttpClientResponseException.class, () -> httpClient.retrieve(request));
        assertEquals(HttpStatus.BAD_REQUEST, responseException.getStatus());
    }

    private static Stream<Arguments> invalidTestCases() {
        return Stream.of(

                // no debitAccount
                Arguments.of("""
                        [
                          {
                            "creditAccount": "06105023389842834748547303",
                            "amount": 10.90
                          },
                          {
                            "debitAccount": "31074318698137062235845814",
                            "creditAccount": "66105036543749403346524547",
                            "amount": 200.90
                          }
                        ]
                        """),

                // no creditAccount
                Arguments.of("""
                        [
                          {
                            "debitAccount": "32309111922661937852684864",
                            "creditAccount": "06105023389842834748547303",
                            "amount": 10.90
                          },
                          {
                            "debitAccount": "31074318698137062235845814",
                            "amount": 200.90
                          }
                        ]
                        """),

                // no amount
                Arguments.of("""
                        [
                          {
                            "debitAccount": "32309111922661937852684864",
                            "creditAccount": "06105023389842834748547303"
                          },
                          {
                            "debitAccount": "31074318698137062235845814",
                            "creditAccount": "66105036543749403346524547",
                            "amount": 200.90
                          }
                        ]
                        """),

                // invalid creditAccount
                Arguments.of("""
                        [
                          {
                            "debitAccount": "31074318698137062235845815",
                            "creditAccount": "06105023389842834748547303",
                            "amount": 10.90
                          },
                          {
                            "debitAccount": "31074318698137062235845814",
                            "creditAccount": "663749403346524547",
                            "amount": 200.90
                          }
                        ]
                        """),

                // invalid debitAccount
                Arguments.of("""
                        [
                          {
                            "debitAccount": "1234",
                            "creditAccount": "06105023389842834748547303",
                            "amount": 10.90
                          },
                          {
                            "debitAccount": "31074318698137062235845814",
                            "creditAccount": "66105036543749403346524547",
                            "amount": 200.90
                          }
                        ]
                        """),

                // invalid json
                Arguments.of("""
                        [
                            "debitAccount": "1234",
                            "creditAccount": "06105023389842834748547303",
                            "amount": 10.90
                          },
                          {
                            "debitAccount": "31074318698137062235845814",
                            "creditAccount": "66105036543749403346524547",
                            "amount": 200.90
                          }
                        ]
                        """));
    }
}
