package org.couchbase.quickstart.springdata.controller;

import java.util.Optional;

import org.couchbase.quickstart.springdata.models.Route;
import org.couchbase.quickstart.springdata.services.RouteService;
import org.springframework.dao.DataRetrievalFailureException;
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

import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.DocumentNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/route")
@Slf4j
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // All Errors
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";
    private static final String DOCUMENT_NOT_FOUND = "Document not found";
    private static final String DOCUMENT_ALREADY_EXISTS = "Document already exists";

    @GetMapping("/{id}")
    @Operation(summary = "Get a route by ID", description = "Get Route by specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to retrieve a document with a specified ID. \n\n Code: [`controllers/RouteController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/RouteController.java) \n File: `RouteController.java` \n Method: `getRoute`", tags = {
            "Route" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route found"),
            @ApiResponse(responseCode = "404", description = "Route not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @Parameter(name = "id", description = "Route ID", required = true, example = "route_10000")
    public ResponseEntity<Route> getRoute(@PathVariable String id) {
        try {
            Optional<Route> route = routeService.getRouteById(id);
            return route.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (DocumentNotFoundException e) {
            log.error(DOCUMENT_NOT_FOUND, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}")
    @Operation(summary = "Create a route", description = "Create a new route.\n\nThis provides an example of using Key Value operations in Couchbase to create a new document with a specified ID. \n\n Code: [`controllers/RouteController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/RouteController.java) \n File: `RouteController.java` \n Method: `createRoute`", tags = {
            "Route" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Route created"),
            @ApiResponse(responseCode = "409", description = "Route already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @Parameter(name = "id", description = "Route ID", required = true, example = "route_10000")
    public ResponseEntity<Route> createRoute(@Valid @RequestBody Route route) {
        try {
            Route newRoute = routeService.createRoute(route);
            return new ResponseEntity<>(newRoute, HttpStatus.CREATED);
        } catch (DocumentExistsException e) {
            log.error(DOCUMENT_ALREADY_EXISTS, e);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a route", description = "Update a route.\n\nThis provides an example of using Key Value operations in Couchbase to update a document with a specified ID. \n\n Code: [`controllers/RouteController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/RouteController.java) \n File: `RouteController.java` \n Method: `updateRoute`", tags = {
            "Route" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route updated"),
            @ApiResponse(responseCode = "404", description = "Route not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @Parameter(name = "id", description = "Route ID", required = true, example = "route_10000")
    public ResponseEntity<Route> updateRoute(@PathVariable String id, @Valid @RequestBody Route route) {
        try {
            Route updatedRoute = routeService.updateRoute(id, route);
            if (updatedRoute != null) {
                return new ResponseEntity<>(updatedRoute, HttpStatus.OK);
            } else {
                log.error(DOCUMENT_NOT_FOUND);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (DocumentNotFoundException e) {
            log.error(DOCUMENT_NOT_FOUND, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a route", description = "Delete a route.\n\nThis provides an example of using Key Value operations in Couchbase to delete a document with a specified ID. \n\n Code: [`controllers/RouteController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/RouteController.java) \n File: `RouteController.java` \n Method: `deleteRoute`", tags = {
            "Route" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Route deleted"),
            @ApiResponse(responseCode = "404", description = "Route not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @Parameter(name = "id", description = "Route ID", required = true, example = "route_10000")
    public ResponseEntity<Void> deleteRoute(@PathVariable String id) {
        try {
            routeService.deleteRoute(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DocumentNotFoundException | DataRetrievalFailureException e) {
            log.error(DOCUMENT_NOT_FOUND, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    @Operation(summary = "List all routes", description = "List all routes.\n\nThis provides an example of using N1QL queries in Couchbase to retrieve all documents of a specified type. \n\n Code: [`controllers/RouteController.java`](https://github.com/couchbase-examples/java-springdata-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springdata/controllers/RouteController.java) \n File: `RouteController.java` \n Method: `listRoutes`", tags = {
            "Route" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routes found"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    public ResponseEntity<Page<Route>> listRoutes(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Route> routes = routeService.getAllRoutes(PageRequest.of(page, size));
            return new ResponseEntity<>(routes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
