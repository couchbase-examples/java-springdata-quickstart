package org.couchbase.quickstart.springdata.services;

import java.util.Optional;

import org.couchbase.quickstart.springdata.models.Airline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AirlineService {

    Page<Airline> getAllAirlines(Pageable pageable);

    Optional<Airline> getAirlineById(String id);

    Airline saveAirline(Airline airline);

    void deleteAirline(String id);

    Airline createAirline(Airline airline);

    Airline updateAirline(String id, Airline airline);

    Page<Airline> findByCountry(String country, Pageable pageable);

    Page<Airline> findByDestinationAirport(String destinationAirport, Pageable pageable);
}
