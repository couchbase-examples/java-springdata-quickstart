package org.couchbase.quickstart.springdata.models;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Field;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Airline implements Serializable {

    @Id
    @NotBlank(message = "Id is mandatory")
    private String id;

    @Field
    @NotBlank(message = "Type is mandatory")
    private String type;

    @Field
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Field
    @NotBlank(message = "IATA code is mandatory")
    private String iata;

    @Field
    @NotBlank(message = "ICAO code is mandatory")
    private String icao;

    @Field
    @NotBlank(message = "Callsign is mandatory")
    private String callsign;

    @Field
    @NotBlank(message = "Country is mandatory")
    private String country;


}
