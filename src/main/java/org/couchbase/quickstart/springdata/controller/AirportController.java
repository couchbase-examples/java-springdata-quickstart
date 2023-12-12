package org.couchbase.quickstart.springdata.controller;


import java.util.Optional;

import org.couchbase.quickstart.springdata.model.Airport;
import org.couchbase.quickstart.springdata.services.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/airport")
public class AirportController {

   @Autowired
   private AirportService airportService;

   @PostMapping("/{id}")
   public ResponseEntity<Airport> createAirport(@PathVariable String id, @RequestBody Airport airport) {
       Airport createdAirport = airportService.createAirport(id, airport);
       return new ResponseEntity<>(createdAirport, HttpStatus.CREATED);
   }

   @GetMapping("/{id}")
   public ResponseEntity<Airport> getAirport(@PathVariable String id) {
       Optional<Airport> airport = airportService.getAirportById(id);
       return airport.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   @PutMapping("/{id}")
   public ResponseEntity<Airport> updateAirport(@PathVariable String id, @RequestBody Airport airport) {
       Airport updatedAirport = airportService.updateAirport(id, airport);
       return new ResponseEntity<>(updatedAirport, HttpStatus.OK);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteAirport(@PathVariable String id) {
       airportService.deleteAirport(id);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

   @GetMapping("/list")
   public ResponseEntity<Page<Airport>> listAirports(Pageable pageable) {
       Page<Airport> airports = airportService.getAllAirports(pageable);
       return new ResponseEntity<>(airports, HttpStatus.OK);
   }

}
