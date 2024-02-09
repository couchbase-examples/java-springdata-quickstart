package org.couchbase.quickstart.springdata.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Field;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Route implements Serializable {

    @Id
    @NotBlank(message = "Id is mandatory")
    private String id;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @NotBlank(message = "Airline is mandatory")
    private String airline;

    @NotBlank(message = "Airline ID is mandatory")
    @Field("airlineid")
    private String airlineId;

    @NotBlank(message = "Source airport is mandatory")
    @Field("sourceairport")
    private String sourceAirport;

    @NotBlank(message = "Destination airport is mandatory")
    @Field("destinationairport")
    private String destinationAirport;

    @NotNull(message = "Stops is mandatory")
    private Integer stops;

    @NotBlank(message = "Equipment is mandatory")
    private String equipment;

    @Valid // To validate the list of schedules
    private List<Schedule> schedule;

    @NotNull(message = "Distance is mandatory")
    private Double distance;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Schedule implements Serializable {
        @NotNull(message = "Day is mandatory")
        private Integer day;

        @NotBlank(message = "Flight is mandatory")
        private String flight;

        @NotBlank(message = "UTC is mandatory")
        private String utc;
    }
}
