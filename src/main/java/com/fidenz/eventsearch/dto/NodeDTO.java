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
public class NodeDTO {
    @JsonProperty("Name")
    private String Name;

    @JsonProperty("IPAddress")
    private String IPAddress;

    @JsonProperty("Plugin")
    private String Plugin;
}
