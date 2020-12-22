package com.fidenz.eventsearch.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventTimeRange {

    @JsonProperty("From")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private DateTime From;

    @JsonProperty("To")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private DateTime To;

    @JsonProperty("Range")
    private long Range;

    @JsonProperty("StartEvent")
    private EventDetail StartEvent;

    @JsonProperty("EndEvent")
    private EventDetail EndEvent;
}
