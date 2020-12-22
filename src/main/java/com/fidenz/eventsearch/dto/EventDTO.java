package com.fidenz.eventsearch.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDTO {

    @JsonProperty("Topic")
    private String Topic;

    @JsonProperty("Params")
    Map<String, Object> Params = new LinkedHashMap<>();

    @JsonAnySetter
    void setParams(String key, Object value) {
        Params.put(key, value);
    }
}
