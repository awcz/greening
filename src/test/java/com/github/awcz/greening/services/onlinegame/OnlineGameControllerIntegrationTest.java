package com.github.awcz.greening.services.onlinegame;

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
class OnlineGameControllerIntegrationTest {

    private final OnlineGameRequestGenerator onlineGameRequestGenerator = new OnlineGameRequestGenerator();

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void shouldReturnExpectedResult() {
        // given
        var input = """
                {
                  "groupCount": 6,
                  "clans": [
                    {
                      "numberOfPlayers": 4,
                      "points": 50
                    },
                    {
                      "numberOfPlayers": 2,
                      "points": 70
                    },
                    {
                      "numberOfPlayers": 6,
                      "points": 60
                    },
                    {
                      "numberOfPlayers": 1,
                      "points": 15
                    },
                    {
                      "numberOfPlayers": 5,
                      "points": 40
                    },
                    {
                      "numberOfPlayers": 3,
                      "points": 45
                    },
                    {
                      "numberOfPlayers": 1,
                      "points": 12
                    },
                    {
                      "numberOfPlayers": 4,
                      "points": 40
                    }
                  ]
                }
                """;
        var request = HttpRequest.POST("/onlinegame/calculate", input);

        // when
        var body = client.toBlocking().retrieve(request);

        // then
        assertNotNull(body);
        assertEquals("""
                    [
                      [
                        {
                          "numberOfPlayers": 2,
                          "points": 70
                        },
                        {
                          "numberOfPlayers": 4,
                          "points": 50
                        }
                      ],
                      [
                        {
                          "numberOfPlayers": 6,
                          "points": 60
                        }
                      ],
                      [
                        {
                          "numberOfPlayers": 3,
                          "points": 45
                        },
                        {
                          "numberOfPlayers": 1,
                          "points": 15
                        },
                        {
                          "numberOfPlayers": 1,
                          "points": 12
                        }
                      ],
                      [
                        {
                          "numberOfPlayers": 4,
                          "points": 40
                        }
                      ],
                      [
                        {
                          "numberOfPlayers": 5,
                          "points": 40
                        }
                      ]
                    ]

                """.replace("\n", "").replace(" ", ""), body);
    }

    @Test
    void shouldProcessRequestWithMaxClansNumber() {
        // given
        var clans = onlineGameRequestGenerator.generateRequest(20_000, 1000);
        var request = HttpRequest.POST("/onlinegame/calculate", clans);

        // when
        var responseBody = client.toBlocking().retrieve(request, List.class);

        // then
        assertFalse(responseBody.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenRequestIsTooLong() {
        // given
        var clans = onlineGameRequestGenerator.generateRequest(20_001, 1000);
        var request = HttpRequest.POST("/onlinegame/calculate", clans);
        var httpClient = client.toBlocking();

        // when
        var responseException = assertThrows(HttpClientResponseException.class, () -> httpClient.retrieve(request));

        // then
        assertEquals(HttpStatus.BAD_REQUEST, responseException.getStatus());
    }

    @ParameterizedTest
    @MethodSource("invalidTestCases")
    void shouldReturnHttp400WhenRequestIsInvalid(String invalidInput) {
        // given
        var request = HttpRequest.POST("/onlinegame/calculate", invalidInput);
        var httpClient = client.toBlocking();

        // when - then
        var responseException = assertThrows(HttpClientResponseException.class, () -> httpClient.retrieve(request));
        assertEquals(HttpStatus.BAD_REQUEST, responseException.getStatus());
    }

    private static Stream<Arguments> invalidTestCases() {
        return Stream.of(

                // no groupCount
                Arguments.of("""
                        {
                          "clans": [
                            {
                              "numberOfPlayers": 4,
                              "points": 40
                            },
                           {
                              "numberOfPlayers": 2,
                              "points": 30
                            }
                          ]
                        }
                        """),

                // numberOfPlayers > groupCount
                Arguments.of("""
                        {
                          "groupCount": 6,
                          "clans": [
                            {
                              "numberOfPlayers": 8,
                              "points": 40
                            }
                          ]
                        }
                        """),

                // no numberOfPlayers
                Arguments.of("""
                        {
                          "groupCount": 6,
                          "clans": [
                            {
                              "points": 40
                            },
                           {
                              "numberOfPlayers": 2,
                              "points": 30
                            }
                          ]
                        }
                        """),

                // no points
                Arguments.of("""
                        {
                          "groupCount": 6,
                          "clans": [
                            {
                              "numberOfPlayers": 4,
                              "points": 40
                            },
                           {
                              "numberOfPlayers": 2
                            }
                          ]
                        }
                        """),

                // groupCount > 1000
                Arguments.of("""
                        {
                          "groupCount": 1001,
                          "clans": [
                            {
                              "numberOfPlayers": 4,
                              "points": 40
                            },
                           {
                              "numberOfPlayers": 2,
                              "points": 50
                            }
                          ]
                        }
                        """),

                // negative groupCount
                Arguments.of("""
                        {
                          "groupCount": -1,
                          "clans": [
                            {
                              "numberOfPlayers": 4,
                              "points": 40
                            },
                           {
                              "numberOfPlayers": 2,
                              "points": 50
                            }
                          ]
                        }
                        """),

                // negative points
                Arguments.of("""
                        {
                          "groupCount": 10,
                          "clans": [
                            {
                              "numberOfPlayers": 4,
                              "points": 40
                            },
                           {
                              "numberOfPlayers": 2,
                              "points": -1
                            }
                          ]
                        }
                        """),

                // negative numberOfPlayers
                Arguments.of("""
                        {
                          "groupCount": 1000,
                          "clans": [
                            {
                              "numberOfPlayers": -4,
                              "points": 40
                            },
                           {
                              "numberOfPlayers": 2,
                               "points": 50
                            }
                          ]
                        }
                        """));
    }
}