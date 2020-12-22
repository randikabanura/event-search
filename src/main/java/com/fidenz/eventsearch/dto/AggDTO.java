package com.fidenz.eventsearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AggDTO {
    @JsonProperty("Name")
    private String Name;

    @JsonProperty("Id")
    private String Id;

    @JsonProperty("IPAddress")
    private String IPAddress;
}
