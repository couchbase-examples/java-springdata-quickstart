package org.couchbase.quickstart.springdata.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Exercises the shared exception-to-status mapping in ApplicationExceptionHandler,
 * so every resource reports a missing document the same way.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class ErrorMappingIntegrationTest {

        private static final String MISSING_ID = "error_mapping_missing_document";

        @Autowired
        private TestRestTemplate restTemplate;

        @ParameterizedTest
        @ValueSource(strings = { "airline", "airport", "route" })
        void getMissingDocumentReturnsNotFound(String resource) {
                ResponseEntity<String> response = restTemplate
                                .getForEntity("/api/v1/" + resource + "/" + MISSING_ID, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @ParameterizedTest
        @ValueSource(strings = { "airline", "airport", "route" })
        void deleteMissingDocumentReturnsNotFound(String resource) {
                ResponseEntity<Void> response = restTemplate.exchange(
                                "/api/v1/" + resource + "/" + MISSING_ID, HttpMethod.DELETE, null, Void.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
}
