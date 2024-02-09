package org.couchbase.quickstart.springdata.controller;

import java.util.Optional;

import jakarta.validation.Valid;

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

    @Operation(summary = "Get an airline by ID")
    @GetMapping("/{id}")
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

    @Operation(summary = "Create an airline")
    @PostMapping("/{id}")
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

    @Operation(summary = "Update an airline")
    @PutMapping("/{id}")
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

    @Operation(summary = "Delete an airline")
    @DeleteMapping("/{id}")
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

    @Operation(summary = "List all airlines")
    @GetMapping("/list")
    public ResponseEntity<Page<Airline>> listAirlines(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Airline> airlines = airlineService.getAllAirlines(PageRequest.of(page, size));
            return new ResponseEntity<>(airlines, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "List all airlines by country")
    @GetMapping("/country/{country}")
    public ResponseEntity<Page<Airline>> listAirlinesByCountry(
            @PathVariable String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {

            Page<Airline> airlines = airlineService.findByCountry(country, PageRequest.of(page, size));
            return new ResponseEntity<>(airlines, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "List all airlines by desination airport")
    @GetMapping("/destination/{destinationAirport}")
    public ResponseEntity<Page<Airline>> listAirlinesByDestinationAirport(
            @PathVariable String destinationAirport,
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
