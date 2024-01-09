package org.couchbase.quickstart.springdata.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.couchbase.quickstart.springdata.models.Airport;
import org.couchbase.quickstart.springdata.models.Airport.Geo;
import org.couchbase.quickstart.springdata.models.RestResponsePage;
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

import com.couchbase.client.core.error.DocumentNotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AirportIntegrationTest {

        @Value("${local.server.port}")
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        private AirportService airportService;

        @BeforeEach
        void setUp() {
                try {
                        if (airportService.getAirportById("airport_create").isPresent()) {
                                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/airport_create");
                        }
                        if (airportService.getAirportById("airport_update").isPresent()) {
                                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/airport_update");
                        }
                        if (airportService.getAirportById("airport_delete").isPresent()) {
                                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/airport_delete");
                        }
                } catch (DocumentNotFoundException | DataRetrievalFailureException e) {
                        System.out.println("Document not found during setup");
                } catch (Exception e) {
                        System.out.println("Error deleting test data during setup");
                }
        }

        @AfterEach
        void tearDown() {
                try {
                        if (airportService.getAirportById("airport_create").isPresent()) {
                                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/airport_create");
                        }
                        if (airportService.getAirportById("airport_update").isPresent()) {
                                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/airport_update");
                        }
                        if (airportService.getAirportById("airport_delete").isPresent()) {
                                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/airport_delete");
                        }
                } catch (DocumentNotFoundException | DataRetrievalFailureException e) {
                        System.out.println("Document not found during setup");
                } catch (Exception e) {
                        System.out.println("Error deleting test data during setup");
                }
        }

        @Test
        void testGetAirport() {
                ResponseEntity<Airport> response = restTemplate
                                .getForEntity("http://localhost:" + port + "/api/v1/airport/airport_1254",
                                                Airport.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airport airport = response.getBody();
                assert airport != null;
                Airport expectedAirport = Airport.builder().id("airport_1254").type("airport")
                                .airportName("Calais Dunkerque")
                                .city("Calais").country("France").faa("CQF").icao("LFAC").tz("Europe/Paris")
                                .geo(new Geo(12.0, 50.962097, 1.954764)).build();
                assertThat(airport).isEqualTo(expectedAirport);
        }

        @Test
        void testCreateAirport() {
                Airport airport = Airport.builder().id("airport_create").type("airport").airportName("Test Airport")
                                .city("Test City").country("Test Country").faa("TST").icao("TEST")
                                .tz("Test Timezone").geo(new Geo(1.0, 2.0, 3.0)).build();
                ResponseEntity<Airport> response = restTemplate.postForEntity(
                                "http://localhost:" + port + "/api/v1/airport/" + airport.getId(), airport,
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
                restTemplate.postForEntity("http://localhost:" + port + "/api/v1/airport/" + airport.getId(), airport,
                                Airport.class);
                restTemplate.put("http://localhost:" + port + "/api/v1/airport/" + airport.getId(), airport);
                ResponseEntity<Airport> response = restTemplate
                                .getForEntity("http://localhost:" + port + "/api/v1/airport/" + airport.getId(),
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
                restTemplate.postForEntity("http://localhost:" + port + "/api/v1/airport/" + airport.getId(), airport,
                                Airport.class);
                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/" + airport.getId());
                ResponseEntity<Airport> response = restTemplate
                                .getForEntity("http://localhost:" + port + "/api/v1/airport/" + airport.getId(),
                                                Airport.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void testListAirports() {
                ResponseEntity<RestResponsePage<Airport>> response = restTemplate.exchange(
                                "http://localhost:" + port + "/api/v1/airport/list", HttpMethod.GET, null,
                                new ParameterizedTypeReference<RestResponsePage<Airport>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                RestResponsePage<Airport> airports = response.getBody();
                assertThat(airports).isNotNull();
                assertThat(airports).hasSize(10);
        }

        // Uncomment this test and modify it similarly if you want to include it
        // @Test
        // void testListDirectConnections() {
        // ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:"
        // + port + "/api/v1/airport/direct-connections?airportCode=test", List.class);
        // assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // // Add more assertions as needed
        // }
}
