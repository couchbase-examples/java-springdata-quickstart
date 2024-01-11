package org.couchbase.quickstart.springdata.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.couchbase.quickstart.springdata.models.Airline;
import org.couchbase.quickstart.springdata.models.RestResponsePage;
import org.couchbase.quickstart.springdata.services.AirlineService;
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
class AirlineIntegrationTest {

        @Value("${local.server.port}")
        private int port;

        @Value("${spring.couchbase.bootstrap-hosts}")
        private String bootstrapHosts;

        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        private AirlineService airlineService;

        private void deleteAirline(String baseUri, String airlineId) {
                try {
                        if (airlineService.getAirlineById(airlineId).isPresent()) {
                                restTemplate.delete(baseUri + "/api/v1/airline/" + airlineId);
                        }
                } catch (DocumentNotFoundException | DataRetrievalFailureException | ResourceAccessException e) {
                        log.warn("Document " + airlineId + " not present prior to test");
                } catch (Exception e) {
                        log.error("Error deleting test data", e.getMessage());
                }
        }

        private void deleteTestAirlineData(String baseUri) {
                deleteAirline(baseUri, "airline_create");
                deleteAirline(baseUri, "airline_update");
                deleteAirline(baseUri, "airline_delete");
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
                deleteTestAirlineData(baseUri);
        }

        @AfterEach
        void tearDown() {
                String baseUri = getBaseUri();
                deleteTestAirlineData(baseUri);
        }

        @Test
        void testGetAirline() {
                ResponseEntity<Airline> response = restTemplate
                                .getForEntity("/api/v1/airline/airline_10", Airline.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airline airline = response.getBody();
                assert airline != null;

                Airline expectedAirline = Airline.builder()
                                .id("airline_10")
                                .type("airline")
                                .name("40-Mile Air")
                                .iata("Q5")
                                .icao("MLA")
                                .callsign("MILE-AIR")
                                .country("United States")
                                .build();
                assertThat(airline).isEqualTo(expectedAirline);
        }

        @Test
        void testCreateAirline() {
                Airline airline = Airline.builder()
                                .id("airline_create")
                                .type("airline")
                                .name("Test Airline")
                                .iata("TA")
                                .icao("TST")
                                .callsign("TEST")
                                .country("United States")
                                .build();
                ResponseEntity<Airline> response = restTemplate.postForEntity(
                                "/api/v1/airline/" + airline.getId(), airline,
                                Airline.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                Airline createdAirline = response.getBody();

                assert createdAirline != null;
                assertThat(createdAirline).isEqualTo(airline);
        }

        @Test
        void testUpdateAirline() {
                Airline airline = Airline.builder()
                                .id("airline_update")
                                .type("airline")
                                .name("Updated Test Airline")
                                .iata("TA")
                                .icao("TST")
                                .callsign("TEST")
                                .country("United States")
                                .build();
                restTemplate.postForEntity("/api/v1/airline/" + airline.getId(), airline,
                                Airline.class);
                restTemplate.put("/api/v1/airline/" + airline.getId(), airline);
                ResponseEntity<Airline> response = restTemplate
                                .getForEntity("/api/v1/airline/" + airline.getId(),
                                                Airline.class);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airline updatedAirline = response.getBody();
                assertThat(updatedAirline)
                                .isNotNull()
                                .isEqualTo(airline);
        }

        @Test
        void testDeleteAirline() {
                String airlineIdToDelete = "airline_delete";
                Airline airline = Airline.builder()
                                .id(airlineIdToDelete)
                                .type("airline")
                                .name("Test Airline")
                                .iata("TA")
                                .icao("TST")
                                .callsign("TEST")
                                .country("United States")
                                .build();
                restTemplate.postForEntity("/api/v1/airline/" + airline.getId(), airline,
                                Airline.class);
                restTemplate.delete("/api/v1/airline/" + airlineIdToDelete);
                ResponseEntity<Airline> response = restTemplate
                                .getForEntity("/api/v1/airline/" + airlineIdToDelete,
                                                Airline.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void testListAirlines() {
                ResponseEntity<RestResponsePage<Airline>> response = restTemplate.exchange(
                                "/api/v1/airline/list", HttpMethod.GET, null,
                                new ParameterizedTypeReference<RestResponsePage<Airline>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                RestResponsePage<Airline> airlines = response.getBody();
                assertThat(airlines).isNotNull();
                assertThat(airlines.getSize()).isEqualTo(10);
        }

        @Test
        void testListAirlinesByCountry() {
                // Check that if it contains
                // airline_10226{"id":10226,"type":"airline","name":"Atifly","iata":"A1","icao":"A1F","callsign":"atifly","country":"United
                // States"}
                String country = "United States";
                ResponseEntity<RestResponsePage<Airline>> response = restTemplate.exchange(
                                "/api/v1/airline/country/" + country,
                                HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<Airline>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                RestResponsePage<Airline> airlines = response.getBody();
                assert airlines != null;

                Airline airline = airlines.stream().filter(a -> a.getId().equals("airline_10226")).findFirst()
                                .orElse(null);
                assertThat(airline).isNotNull();

                Airline expectedAirline = Airline.builder()
                                .id("airline_10226")
                                .type("airline")
                                .name("Atifly")
                                .iata("A1")
                                .icao("A1F")
                                .callsign("atifly")
                                .country("United States")
                                .build();
                assertThat(airline).isEqualTo(expectedAirline);

                // {"id":1191,"type":"airline","name":"Air
                // Austral","iata":"UU","icao":"REU","callsign":"REUNION","country":"France"}

                country = "France";
                ResponseEntity<RestResponsePage<Airline>> response2 = restTemplate.exchange(
                                "/api/v1/airline/country/" + country,
                                HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<Airline>>() {
                                });
                assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

                RestResponsePage<Airline> airlines2 = response2.getBody();
                assert airlines2 != null;
                Airline airline2 = airlines2.stream().filter(a -> a.getId().equals("airline_1191")).findFirst()
                                .orElse(null);

                Airline expectedAirline2 = Airline.builder()
                                .id("airline_1191")
                                .type("airline")
                                .name("Air Austral")
                                .iata("UU")
                                .icao("REU")
                                .callsign("REUNION")
                                .country("France")
                                .build();
                assertThat(airline2).isEqualTo(expectedAirline2);

        }

        @Test
        void testListAirlinesByDestinationAirport() {

                String airport = "LAX";
                ResponseEntity<RestResponsePage<Airline>> response = restTemplate.exchange(
                                "/api/v1/airline/destination/" + airport,
                                HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<Airline>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                RestResponsePage<Airline> airlines = response.getBody();
                assert airlines != null;
                Airline airline = airlines.stream().filter(a -> a.getId().equals("airline_3029")).findFirst()
                                .orElse(null);
                assertThat(airline).isNotNull();

                Airline expectedAirline = Airline.builder()
                                .id("airline_3029")
                                .type("airline")
                                .name("JetBlue Airways")
                                .iata("B6")
                                .icao("JBU")
                                .callsign("JETBLUE")
                                .country("United States")
                                .build();
                assertThat(airline).isEqualTo(expectedAirline);

                airport = "CDG";
                ResponseEntity<RestResponsePage<Airline>> response2 = restTemplate.exchange(
                                "/api/v1/airline/destination/" + airport,
                                HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<Airline>>() {
                                });

                assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
                RestResponsePage<Airline> airlines2 = response2.getBody();

                assert airlines2 != null;

                Airline airline2 = airlines2.stream().filter(a -> a.getId().equals("airline_137")).findFirst()
                                .orElse(null);

                Airline expectedAirline2 = Airline.builder()
                                .id("airline_137")
                                .type("airline")
                                .name("Air France")
                                .iata("AF")
                                .icao("AFR")
                                .callsign("AIRFRANS")
                                .country("France")
                                .build();

                assertThat(airline2).isEqualTo(expectedAirline2);

        }
}
