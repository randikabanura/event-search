package com.fidenz.eventsearch.service;

import com.fidenz.eventsearch.dto.Filter;
import com.fidenz.eventsearch.dto.TimeRange;
import com.fidenz.eventsearch.entity.EventDetail;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

public interface SearchServiceInterface {

//
//    List<Event> getAllData(int page) throws IOException;
//
//    List<Event> getSpecificDocs(String search_string) throws IOException;


    EventDetail findById(String id) throws IOException;

    List<EventDetail> findAll(int page) throws IOException;

    List<EventDetail> search(String query, int page, List<Filter> filters, TimeRange timeRange) throws IOException;
}
