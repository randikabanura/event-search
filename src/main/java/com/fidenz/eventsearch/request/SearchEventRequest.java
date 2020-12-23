package com.fidenz.eventsearch.request;

import com.fidenz.eventsearch.dto.FilterDTO;
import com.fidenz.eventsearch.dto.TimeRangeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchEventRequest {

    private List<FilterDTO> filters = new ArrayList<>();
    private TimeRangeDTO timeRange = new TimeRangeDTO();

    @NonNull
    private String query;

    @NonNull
    @Value("0")
    private int page;
}
