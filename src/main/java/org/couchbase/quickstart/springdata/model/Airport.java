package org.couchbase.quickstart.springdata.model;

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
public class Airport {

    @Id
    private String id;

    @Field
    private String type;

    @Field
    private String airportname;

    @Field
    private String city;

    @Field
    private String country;

    @Field
    private String faa;

    @Field
    private String icao;

    @Field
    private String tz;

    @Field
    private Geo geo;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Geo {
        @Field
        private float alt;
        @Field
        private float lat;
        @Field
        private float lon;
    }
}
