package org.couchbase.quickstart.springdata.services;

import java.util.Optional;

import org.couchbase.quickstart.springdata.models.Airport;
import org.couchbase.quickstart.springdata.repository.AirportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public Page<Airport> getAllAirports(Pageable pageable) {
        return airportRepository.findAll(pageable);
    }

    public Optional<Airport> getAirportById(String id) {
        return airportRepository.findById(id);
    }

    public Airport saveAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    public void deleteAirport(String id) {
        airportRepository.deleteById(id);
    }

    public Airport createAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    public Airport updateAirport(String id, Airport airport) {
        airport.setId(id);
        return airportRepository.save(airport);
    }

    public Page<Airport> getDirectConnections(String id, Pageable pageable) {
        return airportRepository.getDirectConnections(id, pageable);
    }

}
