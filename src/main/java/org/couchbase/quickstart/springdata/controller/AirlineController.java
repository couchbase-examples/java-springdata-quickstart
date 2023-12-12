package org.couchbase.quickstart.springdata.controller;

import java.util.Optional;

import org.couchbase.quickstart.springdata.model.Airline;
import org.couchbase.quickstart.springdata.services.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/v1/airline")
public class AirlineController {

    private AirlineService airlineService;

    @Autowired
    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Airline> createAirline(@PathVariable String id, @RequestBody Airline airline) {
        Airline createdAirline = airlineService.createAirline(id, airline);
        return new ResponseEntity<>(createdAirline, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airline> getAirline(@PathVariable String id) {
        Optional<Airline> airline = airlineService.getAirlineById(id);
        return airline.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Airline> updateAirline(@PathVariable String id, @RequestBody Airline airline) {
        Airline updatedAirline = airlineService.updateAirline(id, airline);
        return new ResponseEntity<>(updatedAirline, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable String id) {
        airlineService.deleteAirline(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Airline>> listAirlines(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Airline> airlines = airlineService.getAllAirlines(PageRequest.of(page, size));
        return new ResponseEntity<>(airlines, HttpStatus.OK);
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<Page<Airline>> listAirlinesByCountry(@PathVariable String country, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Airline> airlines = airlineService.findByCountry(country,PageRequest.of(page, size));
        return new ResponseEntity<>(airlines, HttpStatus.OK);
    }

    @GetMapping("/destination/{destinationAirport}")
    public ResponseEntity<Page<Airline>> listAirlinesByDestinationAirport(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @PathVariable String destinationAirport) {
        Page<Airline> airlines = airlineService.findByDestinationAirport(PageRequest.of(page, size),
                destinationAirport);
        return new ResponseEntity<>(airlines, HttpStatus.OK);
    }

}
