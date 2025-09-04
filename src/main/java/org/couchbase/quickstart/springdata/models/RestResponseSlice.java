package org.couchbase.quickstart.springdata.models;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResponseSlice<T> extends SliceImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestResponseSlice(@JsonProperty("content") List<T> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("pageable") JsonNode pageable,
            @JsonProperty("last") boolean last,
            @JsonProperty("sort") JsonNode sort,
            @JsonProperty("first") boolean first,
            @JsonProperty("numberOfElements") int numberOfElements,
            @JsonProperty("hasNext") boolean hasNext) {

        // Calculate hasNext from the available information
        // For Slice, hasNext is typically determined by whether we have more content
        super(content, PageRequest.of(number, size), hasNext);
    }

}