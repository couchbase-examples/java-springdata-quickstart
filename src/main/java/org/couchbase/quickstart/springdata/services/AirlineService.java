package org.couchbase.quickstart.springdata.services;


import java.util.Optional;

import org.couchbase.quickstart.springdata.model.Airline;
import org.couchbase.quickstart.springdata.repository.AirlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AirlineService {

    @Autowired
    private AirlineRepository airlineRepository;

    public Page<Airline> getAllAirlines(Pageable pageable) {
        return airlineRepository.findAll(pageable);
    }

    public Optional<Airline> getAirlineById(String id) {
        return airlineRepository.findById(id);
    }

    public Airline saveAirline(Airline airline) {
        return airlineRepository.save(airline);
    }

    public void deleteAirline(String id) {
        airlineRepository.deleteById(id);
    }

    public Airline createAirline(String id, Airline airline) {
        airline.setId(id);
        return airlineRepository.save(airline);
    }

    public Airline updateAirline(String id, Airline airline) {
        airline.setId(id);
        return airlineRepository.save(airline);
    }

    public Page<Airline> findByCountry(String country, Pageable pageable) {
        return airlineRepository.findByCountry(country,pageable);
    }

    public Page<Airline> findByDestinationAirport(String destinationAirport, Pageable pageable) {
        return airlineRepository.findByDestinationAirport(destinationAirport, pageable);
    }

}
