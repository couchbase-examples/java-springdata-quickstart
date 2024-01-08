package org.couchbase.quickstart.springdata.services;

import java.util.Optional;

import org.couchbase.quickstart.springdata.models.Airline;
import org.couchbase.quickstart.springdata.repository.AirlineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineServiceImpl(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @Override
    public Page<Airline> getAllAirlines(Pageable pageable) {
        return airlineRepository.findAll(pageable);
    }

    @Override
    public Optional<Airline> getAirlineById(String id) {
        return airlineRepository.findById(id);
    }

    @Override
    public Airline saveAirline(Airline airline) {
        return airlineRepository.save(airline);
    }

    @Override
    public void deleteAirline(String id) {
        airlineRepository.deleteById(id);
    }

    @Override
    public Airline createAirline(Airline airline) {
        return airlineRepository.save(airline);
    }

    @Override
    public Airline updateAirline(String id, Airline airline) {
        airline.setId(id);
        return airlineRepository.save(airline);
    }

    @Override
    public Page<Airline> findByCountry(String country, Pageable pageable) {
        return airlineRepository.findByCountry(country, pageable);
    }

    @Override
    public Page<Airline> findByDestinationAirport(String destinationAirport, Pageable pageable) {
        return airlineRepository.findByDestinationAirport(destinationAirport, pageable);
    }
}
