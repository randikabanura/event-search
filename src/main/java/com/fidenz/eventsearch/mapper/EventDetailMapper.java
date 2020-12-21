package com.fidenz.eventsearch.mapper;

import com.fidenz.eventsearch.dto.AverageCounterDTO;
import com.fidenz.eventsearch.dto.EventDetailDTO;
import com.fidenz.eventsearch.entity.EventDetail;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EventDetailMapper {
    EventDetailMapper INSTANCE = Mappers.getMapper( EventDetailMapper.class );

    @Mapping(source = "messageType", target = "typeMessage")
    EventDetailDTO eventDetailToEventDetailDto(EventDetail eventDetail);

    @MapMapping(keyDateFormat = "")
    List<EventDetailDTO> toListDTO(List<EventDetail> eventDetail);

    @MapMapping(keyDateFormat = "")
    List<EventDetail> toListEntity(List<EventDetailDTO> eventDetailDTO);

    @Mapping(source = "typeMessage", target = "messageType")
    EventDetail eventDetailDtoToEventDetail(EventDetailDTO eventDetailDTO);
}
