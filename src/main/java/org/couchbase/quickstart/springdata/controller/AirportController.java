package org.couchbase.quickstart.springdata.controller;

import java.util.Optional;

import org.couchbase.quickstart.springdata.models.Airport;
import org.couchbase.quickstart.springdata.models.Route;
import org.couchbase.quickstart.springdata.services.AirportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/airport")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an airport by ID", description = "Get Airport by specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to retrieve a document with a specified ID. \n\n Code: [`controllers/AirportController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/AirportController.java) \n File: `AirportController.java` \n Method: `getAirport`", tags = {
            "Airport" })
    @Parameter(name = "id", description = "The ID of the airport to retrieve", required = true, example = "airport_1254")
    public ResponseEntity<Airport> getAirport(@PathVariable String id) {
        Optional<Airport> airport = airportService.getAirportById(id);
        return airport.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}")
    @Operation(summary = "Create an airport", description = "Create an airport with the specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to create a document with a specified ID. \n\n Code: [`controllers/AirportController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/AirportController.java) \n File: `AirportController.java` \n Method: `createAirport`", tags = {
            "Airport" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Airport created"),
            @ApiResponse(responseCode = "409", description = "Airport already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Parameter(name = "id", description = "The ID of the airport to create", required = true, example = "airport_1254")
    public ResponseEntity<Airport> createAirport(@PathVariable String id, @Valid @RequestBody Airport airport) {
        Airport newAirport = airportService.createAirport(airport);
        return new ResponseEntity<>(newAirport, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an airport", description = "Update an airport with the specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to update a document with a specified ID. \n\n Code: [`controllers/AirportController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/AirportController.java) \n File: `AirportController.java` \n Method: `updateAirport`", tags = {
            "Airport" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Airport updated"),
            @ApiResponse(responseCode = "404", description = "Airport not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Parameter(name = "id", description = "The ID of the airport to update", required = true, example = "airport_1254")
    public ResponseEntity<Airport> updateAirport(@PathVariable String id, @Valid @RequestBody Airport airport) {
        Airport updatedAirport = airportService.updateAirport(id, airport);
        return new ResponseEntity<>(updatedAirport, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an airport", description = "Delete an airport with the specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to delete a document with a specified ID. \n\n Code: [`controllers/AirportController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/AirportController.java) \n File: `AirportController.java` \n Method: `deleteAirport`", tags = {
            "Airport" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Airport deleted"),
            @ApiResponse(responseCode = "404", description = "Airport not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Parameter(name = "id", description = "The ID of the airport to delete", required = true, example = "airport_1254")
    public ResponseEntity<Void> deleteAirport(@PathVariable String id) {
        airportService.deleteAirport(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    @Operation(summary = "List all airports", description = "List all airports in the database.\n\nThis provides an example of using N1QL to query all documents in a bucket. \n\n Code: [`controllers/AirportController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/AirportController.java) \n File: `AirportController.java` \n Method: `listAirports`", tags = {
            "Airport" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of airports"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<Airport>> listAirports(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Airport> airports = airportService.getAllAirports(PageRequest.of(page, size));
        return new ResponseEntity<>(airports, HttpStatus.OK);
    }

    @GetMapping("/direct-connections")
    @Operation(summary = "List of direct connections to an airport", description = "List of direct connections to an airport.\n\nThis provides an example of using N1QL to query all documents in a bucket. \n\n Code: [`controllers/AirportController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/AirportController.java) \n File: `AirportController.java` \n Method: `listDirectConnections`", tags = {
            "Airport" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of direct connections"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Parameter(name = "airportCode", description = "The airport code to list direct connections", required = true, example = "SFO")
    public ResponseEntity<Slice<String>> listDirectConnections(
            @RequestParam(required = true) String airportCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Slice<Route> airports = airportService.getDirectConnections(airportCode, PageRequest.of(page, size));
        Slice<String> directConnections = airports.map(Route::getDestinationAirport);
        return new ResponseEntity<>(directConnections, HttpStatus.OK);
    }
}
