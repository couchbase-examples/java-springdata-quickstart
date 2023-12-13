package org.couchbase.quickstart.springdata.repository;

import org.couchbase.quickstart.springdata.model.Airport;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.DynamicProxyable;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.couchbase.repository.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.couchbase.client.java.query.QueryScanConsistency;

@Repository("airportRepository")
@Scope("inventory")
@Collection("airport")
@ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
public interface AirportRepository extends CouchbaseRepository<Airport, String>, DynamicProxyable<AirportRepository> {

    @Query("SELECT META().id AS _ID, type, airportname, city, country, faa, icao, tz, geo AS geo FROM #{#n1ql.bucket} WHERE type = 'airport'")
    Page<Airport> findAll(Pageable pageable);

    @Query("SELECT DISTINCT META(airport).id AS __id,airport.* " +
            "FROM airport as airport " +
            "JOIN route as route ON airport.faa = route.sourceairport " +
            "WHERE airport.faa = $1 AND route.stops = 0")
    Page<Airport> getDirectConnections(String targetAirportCode, Pageable pageable);

}