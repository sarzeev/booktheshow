package com.sarjeev.booktheshow.mappers;

import com.sarjeev.booktheshow.entities.Event;
import com.sarjeev.booktheshow.entities.TicketType;
import com.sarjeev.booktheshow.responses.EventResponse;
import com.sarjeev.booktheshow.responses.TicketTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mapping(target = "organizerId", source = "organizer.id")
    EventResponse toResponse(Event event);

    TicketTypeResponse toTicketTypeResponse(TicketType ticketType);
}
