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
    Optional<Airline> airline = airlineRepository.findById("airline_10"); // SFO
    if (airline.isPresent()) {
      System.out.println("got SFO: " + airline.get());
    } else {
      System.out.println("SFO not found");
    }

    Optional<Airport> airport = airportRepository.findById("airport_1254"); // SFO
    if (airport.isPresent()) {
      System.out.println("got SFO: " + airport.get());
    } else {
      System.out.println("SFO not found");
    }

    Optional<Route> route = routeRepository.findById("route_10000"); // SFO
    if (route.isPresent()) {
      System.out.println("got SFO: " + route.get());
    } else {
      System.out.println("SFO not found");
    }
    try {
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}