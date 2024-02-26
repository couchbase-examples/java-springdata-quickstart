package org.couchbase.quickstart.springdata.repository;

import org.couchbase.quickstart.springdata.models.Airline;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.couchbase.repository.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.couchbase.client.java.query.QueryScanConsistency;

@Scope("inventory")
@Collection("airline")
@Repository
@ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
public interface AirlineRepository extends CouchbaseRepository<Airline, String> {

    @Query("SELECT META(air).id AS __id, air.callsign, air.country, air.iata, air.icao, air.id, air.name, air.type " +
            "FROM airline AS air WHERE air.country = $1")
    Page<Airline> findByCountry(String country, Pageable pageable);

    @Query("SELECT META(air).id AS __id, air.callsign, air.country, air.iata, air.icao, air.id, air.name, air.type " +
            "FROM (SELECT DISTINCT META(airline).id AS airlineId FROM route " +
            "JOIN airline ON route.airlineid = META(airline).id " +
            "WHERE route.destinationairport = $1) AS subquery " +
            "JOIN airline AS air ON META(air).id = subquery.airlineId")
    Page<Airline> findByDestinationAirport(String destinationAirport, Pageable pageable);

}