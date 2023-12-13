package org.couchbase.quickstart.springdata.config;

import java.util.Optional;

import org.couchbase.quickstart.springdata.model.Airline;
import org.couchbase.quickstart.springdata.model.Airport;
import org.couchbase.quickstart.springdata.model.Route;
import org.couchbase.quickstart.springdata.repository.AirlineRepository;
import org.couchbase.quickstart.springdata.repository.AirportRepository;
import org.couchbase.quickstart.springdata.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBSetupRunner implements CommandLineRunner {

  @Autowired
  private AirlineRepository airlineRepository;

  @Autowired
  private AirportRepository airportRepository;

  @Autowired
  private RouteRepository routeRepository;

  @Override
  public void run(String... strings) throws Exception {
    Optional<Airline> airline = airlineRepository.findById("airline_10");
    if (airline.isPresent()) {
      System.out.println("got airline_10: " + airline.get());
    } else {
      System.out.println("airline_10 not found");
    }

    Optional<Airport> airport = airportRepository.findById("airport_1254");
    if (airport.isPresent()) {
      System.out.println("got airport_1254: " + airport.get());
    } else {
      System.out.println("airport_1254 not found");
    }

    Optional<Route> route = routeRepository.findById("route_10000");
    if (route.isPresent()) {
      System.out.println("got route_10000: " + route.get());
    } else {
      System.out.println("route_10000 not found");
    }
    try {
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}