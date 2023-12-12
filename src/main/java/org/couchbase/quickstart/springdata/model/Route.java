package org.couchbase.quickstart.springdata.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Route {

    @Id
    private String id;

    @Field
    private String type;

    @Field
    private String airline;

    @Field("airlineid")
    private String airlineId;

    @Field("sourceairport")
    private String sourceAirport;

    @Field("destinationairport")
    private String destinationAirport;

    @Field
    private int stops;

    @Field
    private String equipment;

    @Field
    private List<Schedule> schedule;

    @Field
    private float distance;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Schedule {
        @Field
        private int day;

        @Field
        private String flight;

        @Field
        private String utc;
    }
}
