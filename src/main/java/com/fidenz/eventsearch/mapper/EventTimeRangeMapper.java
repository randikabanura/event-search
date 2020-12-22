package com.fidenz.eventsearch.mapper;

import com.fidenz.eventsearch.dto.EventTimeRangeDTO;
import com.fidenz.eventsearch.entity.EventTimeRange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventTimeRangeMapper {
    EventTimeRangeMapper INSTANCE = Mappers.getMapper( EventTimeRangeMapper.class );

    @Mapping(source = "startEvent", target = "startEvent")
    EventTimeRangeDTO eventTimeRangeToEventTimeRangeDTO(EventTimeRange eventTimeRange);
}
