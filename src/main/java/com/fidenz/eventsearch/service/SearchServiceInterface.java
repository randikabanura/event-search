package com.fidenz.eventsearch.service;

import com.fidenz.eventsearch.dto.FilterDTO;
import com.fidenz.eventsearch.dto.TimeRangeDTO;
import com.fidenz.eventsearch.entity.EventDetail;

import java.io.IOException;
import java.util.List;

public interface SearchServiceInterface {

//
//    List<Event> getAllData(int page) throws IOException;
//
//    List<Event> getSpecificDocs(String search_string) throws IOException;


    EventDetail findById(String id) throws IOException;

    List<EventDetail> findAll(int page) throws IOException;

    List<EventDetail> search(String query, int page, List<FilterDTO> filters, TimeRangeDTO timeRangeDTO) throws IOException;
}
