package com.fidenz.eventsearch.mapper;

import com.fidenz.eventsearch.dto.AverageCounterDTO;
import com.fidenz.eventsearch.dto.EventDetailDTO;
import com.fidenz.eventsearch.entity.AverageCounter;
import com.fidenz.eventsearch.entity.EventDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AverageCounterMapper {
    AverageCounterMapper INSTANCE = Mappers.getMapper( AverageCounterMapper.class );

    @Mapping(source = "avgForWeek", target = "avgForWeek")
    AverageCounterDTO averageCounterToAverageCounterDTO(AverageCounter averageCounter);
}
