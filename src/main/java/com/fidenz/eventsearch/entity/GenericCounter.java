package com.fidenz.eventsearch.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericCounter {

    @JsonProperty("noOfCameras")
    private long noOfCameras;

    @JsonProperty("noOfEvents")
    private long noOfEvents;

    @JsonProperty("noOfMotionDetectors")
    private long noOfLocations;

    @JsonProperty("noOfMotionDetectors")
    private long noOfMotionDetectors;
}
