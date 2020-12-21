package com.fidenz.eventsearch.presentation;

import com.fidenz.eventsearch.dto.EventDetailDTO;
import com.fidenz.eventsearch.dto.FilterDTO;
import com.fidenz.eventsearch.dto.TimeRangeDTO;
import com.fidenz.eventsearch.entity.EventDetail;

import java.io.IOException;
import java.util.List;

public interface SearchPresentationInterface {
    List<EventDetailDTO> search(String query, int page, List<FilterDTO> filters, TimeRangeDTO timeRangeDTO) throws IOException;
}
