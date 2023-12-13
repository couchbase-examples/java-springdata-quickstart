package org.couchbase.quickstart.springdata.repository;

import org.couchbase.quickstart.springdata.model.Airline;
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
@ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
@Repository("airlineRepository")
public interface AirlineRepository extends CouchbaseRepository<Airline, String> {

    // Create the query to find all airlines

    // @Query("SELECT META().id AS _ID, callsign, country, iata, icao, name, type
    // FROM #{#n1ql.bucket}")
    // @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter}")
    @Query("#{#n1ql.selectEntity}")
    Page<Airline> findAll(Pageable pageable);

    // @Query("SELECT META().id AS _ID, callsign, country, iata, icao, name, type
    // FROM #{#n1ql.bucket} AND country = 'United States'")
    @Query("#{#n1ql.selectEntity} WHERE country = $1")
    Page<Airline> findByCountry(String country, Pageable pageable);

    // @Query("#{#n1ql.selectEntity} FROM (SELECT DISTINCT META(airline).id AS
    // airlineId FROM route " +
    // "JOIN airline ON route.airlineid = META(airline).id " +
    // "WHERE route.destinationairport = $1) AS subquery " +
    // "JOIN #{#n1ql.bucket} AS air ON META(air).id = subquery.airlineId")

    @Query("SELECT META(air).id AS __id, air.callsign, air.country, air.iata, air.icao, air.id, air.name, air.type " +
            "FROM (SELECT DISTINCT META(airline).id AS airlineId FROM route " +
            "JOIN airline ON route.airlineid = META(airline).id " +
            "WHERE route.destinationairport = 'MRS') AS subquery " +
            "JOIN #{#n1ql.bucket} AS air ON META(air).id = subquery.airlineId")
    Page<Airline> findByDestinationAirport(String destinationAirport, Pageable pageable);

}