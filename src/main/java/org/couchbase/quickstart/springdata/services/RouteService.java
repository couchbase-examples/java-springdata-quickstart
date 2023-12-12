package org.couchbase.quickstart.springdata.services;

import java.util.Optional;

import org.couchbase.quickstart.springdata.model.Route;
import org.couchbase.quickstart.springdata.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

   @Autowired
   private RouteRepository routeRepository;

   public Page<Route> getAllRoutes(Pageable pageable) {
       return routeRepository.findAll(pageable);
   }

   public Optional<Route> getRouteById(String id) {
       return routeRepository.findById(id);
   }

   public Route saveRoute(Route route) {
       return routeRepository.save(route);
   }

   public void deleteRoute(String id) {
       routeRepository.deleteById(id);
   }

   public Route createRoute(String id, Route route) {
       route.setId(id);
       return routeRepository.save(route);
   }

   public Route updateRoute(String id, Route route) {
       route.setId(id);
       return routeRepository.save(route);
   }

}
