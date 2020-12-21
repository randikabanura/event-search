package com.fidenz.eventsearch.service;

import com.fidenz.eventsearch.dto.EventDetailDTO;
import com.fidenz.eventsearch.dto.FilterDTO;
import com.fidenz.eventsearch.dto.TimeRangeDTO;
import com.fidenz.eventsearch.entity.EventDetail;

import java.io.IOException;
import java.util.List;

public interface SearchServiceInterface {
    EventDetail findById(String id) throws IOException;
    List<EventDetail> findAll(int page) throws IOException;
    List<EventDetail> search(String query, int page, List<FilterDTO> filters, TimeRangeDTO timeRangeDTO) throws IOException;
}
