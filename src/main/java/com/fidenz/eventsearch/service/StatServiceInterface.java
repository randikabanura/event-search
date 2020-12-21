package com.fidenz.eventsearch.service;

import com.fidenz.eventsearch.dto.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface StatServiceInterface {
    GenericCounterDTO findCounter(List<FilterDTO> filters, TimeRangeDTO timeRangeDTO) throws IOException;
    List<String> findCameras(List<FilterDTO> filters, TimeRangeDTO timeRangeDTO) throws IOException;
    AverageCounterDTO findAverages(List<FilterDTO> filters) throws IOException;
    HashMap<String, Long> findCountByCategory(List<FilterDTO> filters, TimeRangeDTO timeRangeDTO) throws IOException;
    EventTimeRangeDTO findEventTimeRange(String event_start, String event_end, List<FilterDTO> filters, TimeRangeDTO timeRangeDTO) throws IOException;
}
