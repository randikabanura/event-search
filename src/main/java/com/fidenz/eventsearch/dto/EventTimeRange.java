package com.fidenz.eventsearch.dto;

import com.fidenz.eventsearch.entity.EventDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventTimeRange {

    private String from;
    private String to;
    private String range;
    private EventDetail start_event;
    private EventDetail end_event;
}
