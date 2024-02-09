package org.couchbase.quickstart.springdata.repository;

import org.couchbase.quickstart.springdata.models.Airport;
import org.couchbase.quickstart.springdata.models.Route;
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
@Collection("airport")
@Repository
@ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
public interface AirportRepository extends CouchbaseRepository<Airport, String> {

    @Query("SELECT DISTINCT META(route).id as __id,route.* " +
            "FROM airport as airport " +
            "JOIN route as route ON airport.faa = route.sourceairport " +
            "WHERE airport.faa = $1 AND route.stops = 0")
    Page<Route> getDirectConnections(String targetAirportCode, Pageable pageable);

}