package org.couchbase.quickstart.springdata.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.couchbase.quickstart.springdata.models.RestResponsePage;
import org.couchbase.quickstart.springdata.models.Route;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RouteIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetRoute() {
        ResponseEntity<Route> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/v1/route/route_10000", Route.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Route route = response.getBody();
        assertThat(route).isNotNull();
        assertThat(route.getId()).isEqualTo("route_10000");
        assertThat(route.getType()).isEqualTo("route");
        assertThat(route.getAirline()).isEqualTo("AF");
        assertThat(route.getAirlineId()).isEqualTo("airline_137");
        assertThat(route.getSourceAirport()).isEqualTo("TLV");
        assertThat(route.getDestinationAirport()).isEqualTo("MRS");
        assertThat(route.getStops()).isZero();
        assertThat(route.getEquipment()).isEqualTo("320");
        assertThat(route.getSchedule()).hasSize(23);
        assertThat(route.getDistance()).isEqualTo(2881.617376098415);
    }

    @Test
    void testCreateRoute() {
        List<Route.Schedule> schedule = Collections.singletonList(new Route.Schedule(0, "AF198", "10:13:00"));
        Route createdRoute = new Route("route_10001", "route", "AF", "airline_137", "TLV", "MRS", 0, "320",
                  schedule, 2881.617376098415);
        ResponseEntity<Route> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/route/" + createdRoute.getId(), createdRoute, Route.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Route route = response.getBody();
        assertThat(route).isNotNull();
        assertThat(route.getId()).isEqualTo("route_10001");
        assertThat(route.getType()).isEqualTo("route");
        assertThat(route.getAirline()).isEqualTo("AF");
        assertThat(route.getAirlineId()).isEqualTo("airline_137");
        assertThat(route.getSourceAirport()).isEqualTo("TLV");
        assertThat(route.getDestinationAirport()).isEqualTo("MRS");
        assertThat(route.getStops()).isZero();
        assertThat(route.getEquipment()).isEqualTo("320");
        assertThat(route.getSchedule()).hasSize(1);
        assertThat(route.getSchedule().get(0).getDay()).isZero();
        assertThat(route.getSchedule().get(0).getUtc()).isEqualTo("10:13:00");
        assertThat(route.getSchedule().get(0).getFlight()).isEqualTo("AF198");
        assertThat(route.getDistance()).isEqualTo(2881.617376098415);
    }

    @Test
    void testUpdateRoute() {
        List<Route.Schedule> schedule = Arrays.asList(new Route.Schedule(0, "AF198", "10:13:00"),
                new Route.Schedule(1, "AF547", "19:14:00"), new Route.Schedule(2, "AF943", "01:31:00"),
                new Route.Schedule(3, "AF356", "12:40:00"), new Route.Schedule(4, "AF480", "08:58:00"),
                new Route.Schedule(5, "AF250", "12:59:00"), new Route.Schedule(6, "AF130", "04:45:00"),
                new Route.Schedule(7, "AF997", "00:31:00"), new Route.Schedule(8, "AF223", "19:41:00"),
                new Route.Schedule(9, "AF890", "15:14:00"), new Route.Schedule(10, "AF399", "00:30:00"),
                new Route.Schedule(11, "AF328", "16:18:00"), new Route.Schedule(12, "AF074", "23:50:00"),
                new Route.Schedule(13, "AF556", "11:33:00"), new Route.Schedule(14, "AF064", "13:23:00"),
                new Route.Schedule(15, "AF596", "12:09:00"), new Route.Schedule(16, "AF818", "08:02:00"),
                new Route.Schedule(17, "AF967", "11:33:00"), new Route.Schedule(18, "AF730", "19:42:00"),
                new Route.Schedule(19, "AF882", "17:07:00"), new Route.Schedule(20, "AF485", "17:03:00"),
                new Route.Schedule(21, "AF898", "10:01:00"), new Route.Schedule(22, "AF496", "07:00:00"));
        Route route = new Route("route_10001", "route", "AF", "airline_137", "TLV", "MRS", 0, "320", schedule, 2881.617376098415);
        restTemplate.put("http://localhost:" + port + "/api/v1/route/" + route.getId(), route);
        ResponseEntity<Route> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/v1/route/" + route.getId(), Route.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Route updatedRoute = response.getBody();
        assertThat(updatedRoute).isNotNull();
        assertThat(updatedRoute.getId()).isEqualTo("route_10001");
        assertThat(updatedRoute.getType()).isEqualTo("route");
        assertThat(updatedRoute.getAirline()).isEqualTo("AF");
        assertThat(updatedRoute.getAirlineId()).isEqualTo("airline_137");
        assertThat(updatedRoute.getSourceAirport()).isEqualTo("TLV");
        assertThat(updatedRoute.getDestinationAirport()).isEqualTo("MRS");
        assertThat(updatedRoute.getStops()).isZero();
        assertThat(updatedRoute.getEquipment()).isEqualTo("320");
        assertThat(updatedRoute.getDistance()).isEqualTo(2881.617376098415);
        assertThat(updatedRoute.getSchedule()).hasSize(23);
    }

    @Test
    void testDeleteRoute() {
        Route route = new Route("route_10001", "route", "AF", "airline_137", "TLV", "MRS", 0, "320", null, 2881.617376098415);
        restTemplate.postForEntity("http://localhost:" + port + "/api/v1/route/" + route.getId(), route, Route.class);
        restTemplate.delete("http://localhost:" + port + "/api/v1/route/" + route.getId());
        ResponseEntity<Route> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/v1/route/" + route.getId(), Route.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testListRoutes() {
        ResponseEntity<RestResponsePage<Route>> response = restTemplate.exchange("http://localhost:" + port + "/api/v1/route/list",
                HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<Route>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        RestResponsePage<Route> routes = response.getBody();
        assertThat(routes).isNotNull();
        assertThat(routes.getTotalElements()).isGreaterThan(100);
    }
}