package com.fidenz.eventsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericCounterDTO {
    private long noOfCameras;
    private long noOfEvents;
    private long noOfLocations;
    private long noOfMotionDetectors;

    public void setNoOfMotionDetectors(long noOfMotionDetectors) {
        this.noOfMotionDetectors = noOfMotionDetectors;
    }

    public void setNoOfCameras(long noOfCameras) {
        this.noOfCameras = noOfCameras;
    }

    public void setNoOfEvents(long noOfEvents) {
        this.noOfEvents = noOfEvents;
    }

    public void setNoOfLocations(long noOfLocations) {
        this.noOfLocations = noOfLocations;
    }
}