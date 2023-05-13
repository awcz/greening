package com.github.awcz.greening.services.atmservice;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class AtmControllerIntegrationTest {

    @Inject
    @Client("/")
    HttpClient client;

    @ParameterizedTest
    @MethodSource("validTestCases")
    void shouldReturnExpectedResult(String input, String expectedResponse) {
        // given
        var request = HttpRequest.POST("/atms/calculateOrder", input);

        // when
        var body = client.toBlocking().retrieve(request);

        // then
        assertNotNull(body);
        assertEquals(expectedResponse.replaceAll("[\n ]", ""), body);
    }

    private static Stream<Arguments> validTestCases() {
        return Stream.of(validTestCase1(), validTestCase2());
    }

    @ParameterizedTest
    @MethodSource("invalidTestCases")
    void shouldReturnHttp400WhenRequestIsInvalid(String invalidInput) {
        // given
        var request = HttpRequest.POST("/atms/calculateOrder", invalidInput);
        var httpClient = client.toBlocking();

        // when - then
        var responseException = assertThrows(HttpClientResponseException.class,
                () -> httpClient.retrieve(request));
        assertEquals(HttpStatus.BAD_REQUEST, responseException.getStatus());
    }

    private static Arguments validTestCase1() {
        return Arguments.of("""
                [
                  {
                    "region": 4,
                    "requestType": "STANDARD",
                    "atmId": 1
                  },
                  {
                    "region": 1,
                    "requestType": "STANDARD",
                    "atmId": 1
                  },
                  {
                    "region": 2,
                    "requestType": "STANDARD",
                    "atmId": 1
                  },
                  {
                    "region": 3,
                    "requestType": "PRIORITY",
                    "atmId": 2
                  },
                  {
                    "region": 3,
                    "requestType": "STANDARD",
                    "atmId": 1
                  },
                  {
                    "region": 2,
                    "requestType": "SIGNAL_LOW",
                    "atmId": 1
                  },
                  {
                    "region": 5,
                    "requestType": "STANDARD",
                    "atmId": 2
                  },
                  {
                    "region": 5,
                    "requestType": "FAILURE_RESTART",
                    "atmId": 1
                  }
                ]
                """, """
                [
                  {
                    "region": 1,
                    "atmId": 1
                  },
                  {
                    "region": 2,
                    "atmId": 1
                  },
                  {
                    "region": 3,
                    "atmId": 2
                  },
                  {
                    "region": 3,
                    "atmId": 1
                  },
                  {
                    "region": 4,
                    "atmId": 1
                  },
                  {
                    "region": 5,
                    "atmId": 1
                  },
                  {
                    "region": 5,
                    "atmId": 2
                  }
                ]
                           
                """);
    }

    private static Arguments validTestCase2() {
        return Arguments.of("""
                [
                  {
                    "region": 1,
                    "requestType": "STANDARD",
                    "atmId": 2
                  },
                  {
                    "region": 1,
                    "requestType": "STANDARD",
                    "atmId": 1
                  },
                  {
                    "region": 2,
                    "requestType": "PRIORITY",
                    "atmId": 3
                  },
                  {
                    "region": 3,
                    "requestType": "STANDARD",
                    "atmId": 4
                  },
                  {
                    "region": 4,
                    "requestType": "STANDARD",
                    "atmId": 5
                  },
                  {
                    "region": 5,
                    "requestType": "PRIORITY",
                    "atmId": 2
                  },
                  {
                    "region": 5,
                    "requestType": "STANDARD",
                    "atmId": 1
                  },
                  {
                    "region": 3,
                    "requestType": "SIGNAL_LOW",
                    "atmId": 2
                  },
                  {
                    "region": 2,
                    "requestType": "SIGNAL_LOW",
                    "atmId": 1
                  },
                  {
                    "region": 3,
                    "requestType": "FAILURE_RESTART",
                    "atmId": 1
                  }
                ]
                """, """
                [
                  {
                    "region": 1,
                    "atmId": 2
                  },
                  {
                    "region": 1,
                    "atmId": 1
                  },
                  {
                    "region": 2,
                    "atmId": 3
                  },
                  {
                    "region": 2,
                    "atmId": 1
                  },
                  {
                    "region": 3,
                    "atmId": 1
                  },
                  {
                    "region": 3,
                    "atmId": 2
                  },
                  {
                    "region": 3,
                    "atmId": 4
                  },
                  {
                    "region": 4,
                    "atmId": 5
                  },
                  {
                    "region": 5,
                    "atmId": 2
                  },
                  {
                    "region": 5,
                    "atmId": 1
                  }
                ]
                """);
    }

    private static Stream<Arguments> invalidTestCases() {
        return Stream.of(
                // no atmId
                Arguments.of("""
                        [{
                          "region": 4,
                          "requestType": "STANDARD",
                          "atmId": 1
                        },
                        {
                          "region": 1,
                          "requestType": "STANDARD"
                        }]
                        """),
                // region > 9999
                Arguments.of("""
                        [{
                          "region": 4,
                          "requestType": "STANDARD",
                          "atmId": 1
                        },
                        {
                          "region": 10000,
                          "requestType": "STANDARD",
                          "atmId": 1
                        }]
                        """),
                // atmId > 9999
                Arguments.of("""
                        [{
                          "region": 4,
                          "requestType": "STANDARD",
                          "atmId": 1
                        },
                        {
                          "region": 1,
                          "requestType": "STANDARD",
                          "atmId": 10000
                        }]
                        """),
                // no requestType
                Arguments.of("""
                        [{
                          "region": 4,
                          "atmId": 1
                        },
                        {
                          "region": 1,
                          "requestType": "STANDARD",
                          "atmId": 1
                        }]
                        """),
                // no region
                Arguments.of("""
                        [{
                          "requestType": "STANDARD",
                          "atmId": 1
                        },
                        {
                          "region": 1,
                          "requestType": "STANDARD",
                          "atmId": 1
                        }]
                        """),

                // null
                Arguments.of("""
                        """));
    }
}
