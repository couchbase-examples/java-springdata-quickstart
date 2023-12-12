package org.couchbase.quickstart.springdata.repository;

import org.couchbase.quickstart.springdata.model.Route;
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

@Repository("routeRepository")
@Scope("inventory")
@Collection("route")
@ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
public interface RouteRepository extends CouchbaseRepository<Route, String>, DynamicProxyable<RouteRepository> {

    @Query("SELECT META().id AS _ID, type, airline, airlineid, sourceairport, destinationairport, stops, equipment, schedule FROM #{#n1ql.bucket} WHERE type = 'route'")
    Page<Route> findAll(Pageable pageable);
}
