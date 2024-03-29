package org.couchbase.quickstart.springdata.services;

import java.util.Optional;

import org.couchbase.quickstart.springdata.models.Route;
import org.couchbase.quickstart.springdata.repository.RouteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

   private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
         this.routeRepository = routeRepository;
    }

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

   public Route createRoute(Route route) {
       return routeRepository.save(route);
   }

   public Route updateRoute(String id, Route route) {
       route.setId(id);
       return routeRepository.save(route);
   }

}
