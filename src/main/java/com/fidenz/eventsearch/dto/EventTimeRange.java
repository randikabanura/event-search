package com.fidenz.eventsearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fidenz.eventsearch.entity.EventDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
