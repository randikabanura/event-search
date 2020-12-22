package com.fidenz.eventsearch.mapper;

import com.fidenz.eventsearch.dto.GenericCounterDTO;
import com.fidenz.eventsearch.entity.GenericCounter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GenericCounterMapper {
    GenericCounterMapper INSTANCE = Mappers.getMapper( GenericCounterMapper.class );

    @Mapping(source = "noOfMotionDetectors", target = "noOfMotionDetectors")
    GenericCounterDTO genericCounterToGenericCounterDTO(GenericCounter genericCounter);
}
