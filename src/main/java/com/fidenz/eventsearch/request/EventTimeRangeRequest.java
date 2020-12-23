package com.fidenz.eventsearch.request;

import com.fidenz.eventsearch.dto.FilterDTO;
import com.fidenz.eventsearch.dto.TimeRangeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventTimeRangeRequest {
    private List<FilterDTO> filters = new ArrayList<>();
    private TimeRangeDTO timeRange = new TimeRangeDTO();

    @NonNull
    private String eventStart;

    @NonNull
    private String eventEnd;
}
