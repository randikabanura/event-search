package com.fidenz.eventsearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fidenz.eventsearch.entity.Agg;
import com.fidenz.eventsearch.entity.Event;
import com.fidenz.eventsearch.entity.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDetailDTO {
    @JsonProperty("Timestamp")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private DateTime Timestamp;

    @JsonProperty("TypeMessage")
    private String TypeMessage;

    @Id
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("Node")
    private NodeDTO Node;

    @JsonProperty("Agg")
    private AggDTO Agg;

    @JsonProperty("Event")
    private EventDTO Event;
}
