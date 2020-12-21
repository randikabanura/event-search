package com.fidenz.eventsearch.service;

import com.fidenz.eventsearch.dto.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface StatServiceInterface {
    GenericCounter findCounter(List<Filter> filters, TimeRange timeRange) throws IOException;
    List<String> findCameras(List<Filter> filters, TimeRange timeRange) throws IOException;
    AverageCounter findAverages(List<Filter> filters) throws IOException;
    HashMap<String, Long> findCountByCategory(List<Filter> filters, TimeRange timeRange) throws IOException;
    EventTimeRange findEventTimeRange(List<Filter> filters, TimeRange timeRange) throws IOException;
}
