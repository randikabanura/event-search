package com.fidenz.eventsearch.presentation;

import com.fidenz.eventsearch.dto.AverageCounterDTO;
import com.fidenz.eventsearch.dto.FilterDTO;
import com.fidenz.eventsearch.dto.TimeRangeDTO;
import com.fidenz.eventsearch.service.StatServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class StatPresentationImpl implements StatPresentationInterface{

    @Autowired
    private StatServiceInterface statService;

    public AverageCounterDTO getAverages(List<FilterDTO> filters) throws IOException {
        return this.statService.findAverages(filters);
    }
}
