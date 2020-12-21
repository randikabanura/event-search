package com.fidenz.eventsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngestStatusDTO {
    private boolean Status;
    private String Message;
}
