package com.fidenz.eventsearch.presentation;

import com.fidenz.eventsearch.dto.EventDetailDTO;
import com.fidenz.eventsearch.dto.FilterDTO;
import com.fidenz.eventsearch.dto.TimeRangeDTO;
import com.fidenz.eventsearch.mapper.EventDetailMapper;
import com.fidenz.eventsearch.service.SearchServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SearchPresentationImpl implements SearchPresentationInterface{

    @Autowired
    private SearchServiceInterface searchServiceInterface;

    @Override
    public List<EventDetailDTO> search(String query, int page, List<FilterDTO> filters, TimeRangeDTO timeRangeDTO) throws IOException {
        return EventDetailMapper.INSTANCE.toListDTO(this.searchServiceInterface.search(query, page, filters, timeRangeDTO));
    }

    @Override
    public EventDetailDTO findById(String id) throws IOException {
        return EventDetailMapper.INSTANCE.eventDetailToEventDetailDto(this.searchServiceInterface.findById(id));
    }

    @Override
    public List<EventDetailDTO> findAll(int page) throws IOException {
        return EventDetailMapper.INSTANCE.toListDTO(this.searchServiceInterface.findAll(page));
    }


}
