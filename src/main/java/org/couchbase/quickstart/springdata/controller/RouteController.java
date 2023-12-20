package org.couchbase.quickstart.springdata.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.couchbase.quickstart.springdata.models.Route;
import org.couchbase.quickstart.springdata.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping("/{id}")
    public ResponseEntity<Route> createRoute(@PathVariable String id, @Valid @RequestBody Route route) {
        Route createdRoute = routeService.createRoute(id, route);
        return new ResponseEntity<>(createdRoute, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getRoute(@PathVariable String id) {
        Optional<Route> route = routeService.getRouteById(id);
        return route.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Route> updateRoute(@PathVariable String id, @Valid @RequestBody Route route) {
        Route updatedRoute = routeService.updateRoute(id, route);
        return new ResponseEntity<>(updatedRoute, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable String id) {
        routeService.deleteRoute(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Route>> listRoutes(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Route> routes = routeService.getAllRoutes(PageRequest.of(page, size));
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

}
