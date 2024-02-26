package org.couchbase.quickstart.springdata.controller;

import java.util.Optional;

import org.couchbase.quickstart.springdata.models.Airline;
import org.couchbase.quickstart.springdata.services.AirlineService;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.DocumentNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/airline")
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    // All Errors
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";
    private static final String DOCUMENT_NOT_FOUND = "Document not found";
    private static final String DOCUMENT_ALREADY_EXISTS = "Document already exists";

    @GetMapping("/{id}")
    @Operation(summary = "Get an airline by ID", description = "Get Airline by specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to retrieve a document with a specified ID. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `getAirline`", tags = {
            "Airline" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Airline found"),
            @ApiResponse(responseCode = "404", description = "Airline not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @Parameter(name = "id", description = "Airline ID", required = true, example = "airline_10")
    public ResponseEntity<Airline> getAirline(@PathVariable String id) {
        try {
            Optional<Airline> airline = airlineService.getAirlineById(id);
            return airline.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (DocumentNotFoundException e) {
            log.error(DOCUMENT_NOT_FOUND, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}")
    @Operation(summary = "Create an airline", description = "Create an airline with the specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to create a document with a specified ID. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `createAirline`", tags = {
            "Airline" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Airline created"),
            @ApiResponse(responseCode = "409", description = "Airline already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @Parameter(name = "id", description = "Airline ID", required = true, example = "airline_10")
    public ResponseEntity<Airline> createAirline(@Valid @RequestBody Airline airline) {
        try {
            Airline newAirline = airlineService.createAirline(airline);
            return new ResponseEntity<>(newAirline, HttpStatus.CREATED);
        } catch (DocumentExistsException e) {
            log.error(DOCUMENT_ALREADY_EXISTS, e);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an airline", description = "Update an airline with the specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to update a document with a specified ID. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `updateAirline`", tags = {
            "Airline" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Airline updated"),
            @ApiResponse(responseCode = "404", description = "Airline not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @Parameter(name = "id", description = "Airline ID", required = true, example = "airline_10")
    public ResponseEntity<Airline> updateAirline(@PathVariable String id, @Valid @RequestBody Airline airline) {
        try {
            Airline updatedAirline = airlineService.updateAirline(id, airline);
            if (updatedAirline != null) {
                return new ResponseEntity<>(updatedAirline, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (DocumentNotFoundException | DataRetrievalFailureException e) {
            log.error(DOCUMENT_NOT_FOUND, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an airline", description = "Delete an airline with the specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to delete a document with a specified ID. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `deleteAirline`", tags = {
            "Airline" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Airline deleted"),
            @ApiResponse(responseCode = "404", description = "Airline not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @Parameter(name = "id", description = "Airline ID", required = true, example = "airline_10")
    public ResponseEntity<Void> deleteAirline(@PathVariable String id) {
        try {
            airlineService.deleteAirline(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DocumentNotFoundException | DataRetrievalFailureException e) {
            log.error(DOCUMENT_NOT_FOUND, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    @Operation(summary = "List all airlines by country", description = "List all airlines by country.\n\nThis provides an example of using N1QL queries in Couchbase to retrieve documents with a specified field value. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `listAirlinesByCountry`", tags = {
            "Airline" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Airlines found"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @Parameter(name = "country", description = "Country", required = false, example = "United States")
    public ResponseEntity<Page<Airline>> listAirlinesByCountry(
            @RequestParam(required = false) String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (country == null || country.isEmpty()) {
                Page<Airline> airlines = airlineService.getAllAirlines(PageRequest.of(page, size));
                return new ResponseEntity<>(airlines, HttpStatus.OK);
            } else {
                Page<Airline> airlines = airlineService.findByCountry(country, PageRequest.of(page, size));
                return new ResponseEntity<>(airlines, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/to-airport")
    @Operation(summary = "List all airlines by desination airport", description = "List all airlines by destination airport.\n\nThis provides an example of using N1QL queries in Couchbase to retrieve documents with a specified field value. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `listAirlinesByDestinationAirport`", tags = {
            "Airline" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Airlines found"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @Parameter(name = "destinationAirport", description = "Destination Airport", required = false, example = "SFO")
    public ResponseEntity<Page<Airline>> listAirlinesByDestinationAirport(
            @RequestParam(required = false) String destinationAirport,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Airline> airlines = airlineService.findByDestinationAirport(destinationAirport,
                    PageRequest.of(page, size));

            return new ResponseEntity<>(airlines, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
