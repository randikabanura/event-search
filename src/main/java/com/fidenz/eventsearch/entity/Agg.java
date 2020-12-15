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
public class Agg {
    @JsonProperty("Name")
    private String Name;

    @JsonProperty("Id")
    private String Id;

    @JsonProperty("IPAddress")
    private String IPAddress;
}
