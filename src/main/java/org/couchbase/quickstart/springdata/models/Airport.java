package org.couchbase.quickstart.springdata.models;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Airport implements Serializable {

    @Id
    @NotBlank(message = "Id is mandatory")
    private String id;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @NotBlank(message = "Airport name is mandatory")
    @Field("airportname")
    private String airportName;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "Country is mandatory")
    private String country;

    @NotBlank(message = "FAA code is mandatory")
    @Pattern(regexp = "^[A-Z]{3}$", message = "FAA code must be a 3-letter uppercase code")
    private String faa;

    @NotBlank(message = "ICAO code is mandatory")
    @Pattern(regexp = "^[A-Z]{4}$", message = "ICAO code must be a 4-letter uppercase code")
    private String icao;

    @NotBlank(message = "Timezone is mandatory")
    private String tz;

    @Valid // To validate the embedded Geo object
    private Geo geo;

    @Document
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Geo implements Serializable {

        @NotNull(message = "Altitude is mandatory")
        private Double alt;

        @NotNull(message = "Latitude is mandatory")
        private Double lat;

        @NotNull(message = "Longitude is mandatory")
        private Double lon;
    }
}
