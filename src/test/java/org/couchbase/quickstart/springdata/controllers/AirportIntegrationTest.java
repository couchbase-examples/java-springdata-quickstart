package org.couchbase.quickstart.springdata.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.couchbase.quickstart.springdata.models.Airport;
import org.couchbase.quickstart.springdata.models.Airport.Geo;
import org.couchbase.quickstart.springdata.models.RestResponsePage;
import org.couchbase.quickstart.springdata.models.RestResponseSlice;
import org.couchbase.quickstart.springdata.services.AirportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import com.couchbase.client.core.error.DocumentNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AirportIntegrationTest {

        @Value("${local.server.port}")
        private int port;

        @Value("#{systemEnvironment['DB_CONN_STR'] ?: '${spring.couchbase.connection-string:localhost}'}")
        private String bootstrapHosts;

        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        private AirportService airportService;

        private void deleteAirport(String baseUri, String airportId) {
                try {
                        if (airportService.getAirportById(airportId).isPresent()) {
                                restTemplate.delete(baseUri + "/api/v1/airport/" + airportId);
                        }
                } catch (DocumentNotFoundException | DataRetrievalFailureException | ResourceAccessException e) {
                        log.warn("Document {} not present prior to test", airportId);
                } catch (Exception e) {
                        log.debug("Cleanup: Could not delete test airport {}: {} (this is expected during test cleanup)", airportId, e.getMessage());
                        // Continue with cleanup even if one deletion fails
                }
        }

        private void deleteTestAirportData(String baseUri) {
                deleteAirport(baseUri, "airport_create");
                deleteAirport(baseUri, "airport_update");
                deleteAirport(baseUri, "airport_delete");
        }

        private String getBaseUri() {
                String baseUri = "";
                if (bootstrapHosts.contains("localhost")) {
                        baseUri = "http://localhost:" + port;
                } else {
                        baseUri = bootstrapHosts;
                }
                return baseUri;
        }

        @BeforeEach
        void setUp() {
                String baseUri = getBaseUri();
                deleteTestAirportData(baseUri);
        }

        @AfterEach
        void tearDown() {
                String baseUri = getBaseUri();
                deleteTestAirportData(baseUri);
        }

        @Test
        void testGetAirport() {
                ResponseEntity<Airport> response = restTemplate
                                .getForEntity("/api/v1/airport/airport_1255",
                                                Airport.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airport airport = response.getBody();
                assert airport != null;
                Airport expectedAirport = Airport.builder()
                                .id("airport_1255")
                                .type("airport")
                                .airportName("Peronne St Quentin")
                                .city("Peronne")
                                .country("France")
                                .faa(null)
                                .icao("LFAG").tz("Europe/Paris")
                                .geo(Geo.builder()
                                                .lat(49.868547)
                                                .lon(3.029578)
                                                .alt(295.0)
                                                .build())
                                .build();
                assertThat(airport).isEqualTo(expectedAirport);
        }

        @Test
        void testCreateAirport() {
                Airport airport = Airport.builder().id("airport_create").type("airport").airportName("Test Airport")
                                .city("Test City").country("Test Country").faa("TST").icao("TEST")
                                .tz("Test Timezone").geo(new Geo(1.0, 2.0, 3.0)).build();
                ResponseEntity<Airport> response = restTemplate.postForEntity(
                                "/api/v1/airport/" + airport.getId(), airport,
                                Airport.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                Airport createdAirport = response.getBody();
                assert createdAirport != null;
                assertThat(createdAirport).isEqualTo(airport);
        }

        @Test
        void testUpdateAirport() {
                Airport airport = Airport.builder().id("airport_update").type("airport")
                                .airportName("Updated Test Airport").city("Updated Test City")
                                .country("Updated Test Country").faa("TST").icao("TEST")
                                .tz("Updated Test Timezone").geo(new Geo(1.0, 2.0, 3.0)).build();
                restTemplate.postForEntity("/api/v1/airport/" + airport.getId(), airport,
                                Airport.class);
                restTemplate.put("/api/v1/airport/" + airport.getId(), airport);
                ResponseEntity<Airport> response = restTemplate
                                .getForEntity("/api/v1/airport/" + airport.getId(),
                                                Airport.class);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airport updatedAirport = response.getBody();
                assert updatedAirport != null;
                assertThat(updatedAirport).isEqualTo(airport);
        }

        @Test
        void testDeleteAirport() {
                Airport airport = Airport.builder().id("airport_delete").type("airport").airportName("Test Airport")
                                .city("Test City").country("Test Country").faa("TST").icao("TEST")
                                .tz("Test Timezone").geo(new Geo(1.0, 2.0, 3.0)).build();
                restTemplate.postForEntity("/api/v1/airport/" + airport.getId(), airport,
                                Airport.class);
                restTemplate.delete("/api/v1/airport/" + airport.getId());
                ResponseEntity<Airport> response = restTemplate
                                .getForEntity("/api/v1/airport/" + airport.getId(),
                                                Airport.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void testListAirports() {
                ResponseEntity<RestResponsePage<Airport>> response = restTemplate.exchange(
                                "/api/v1/airport/list", HttpMethod.GET, null,
                                new ParameterizedTypeReference<RestResponsePage<Airport>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                RestResponsePage<Airport> airports = response.getBody();
                assertThat(airports).isNotNull();
                assertThat(airports).hasSize(10);
        }

        @Test
        void testListDirectConnections() {
                String airportCode = "LAX";
                ResponseEntity<RestResponseSlice<String>> response = restTemplate.exchange(
                                "/api/v1/airport/direct-connections?airportCode=" + airportCode + "&page=0&size=10",
                                HttpMethod.GET, null, new ParameterizedTypeReference<RestResponseSlice<String>>() {
                                });

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                RestResponseSlice<String> directConnections = response.getBody();

                assertThat(directConnections).isNotNull().hasSize(10);
                assertThat(directConnections).contains("NRT", "CUN", "GDL", "HMO", "MEX", "MZT", "PVR", "SJD", "ZIH",
                                "ZLO");

                airportCode = "JFK";
                response = restTemplate.exchange(
                                "/api/v1/airport/direct-connections?airportCode=" + airportCode + "&page=0&size=10",
                                HttpMethod.GET, null, new ParameterizedTypeReference<RestResponseSlice<String>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                directConnections = response.getBody();

                assertThat(directConnections).isNotNull().hasSize(10);
                assertThat(directConnections).contains("DEL", "LHR", "EZE", "ATL", "CUN", "MEX", "EZE", "LAX", "SAN",
                                "SEA");

        }
}
