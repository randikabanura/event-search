package com.fidenz.eventsearch.presentation;

import com.fidenz.eventsearch.dto.AverageCounterDTO;
import com.fidenz.eventsearch.dto.FilterDTO;
import com.fidenz.eventsearch.dto.TimeRangeDTO;

import java.io.IOException;
import java.util.List;

public interface StatPresentationInterface {
    AverageCounterDTO getAverages(List<FilterDTO> filters) throws IOException;
}
