package com.fidenz.eventsearch.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "event_detail")
public class EventDetail {

    @JsonProperty("Timestamp")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private DateTime Timestamp;

    @JsonProperty("MessageType")
    private String MessageType;

    @Id
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("Node")
    private Node Node;

    @JsonProperty("Agg")
    private Agg Agg;

    @JsonProperty("Event")
    private Event Event;
}
