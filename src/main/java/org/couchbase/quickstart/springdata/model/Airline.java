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
public class Airline {

   @Id
    private String id;

    @Field
    private String type;

    @Field
    private String name;

    @Field
    private String iata;

    @Field
    private String icao;

    @Field
    private String callsign;

    @Field
    private String country;

}
