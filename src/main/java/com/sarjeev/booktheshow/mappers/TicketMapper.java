package com.sarjeev.booktheshow.mappers;

import com.sarjeev.booktheshow.entities.QrCode;
import com.sarjeev.booktheshow.entities.Ticket;
import com.sarjeev.booktheshow.entities.TicketSale;
import com.sarjeev.booktheshow.entities.TicketValidation;
import com.sarjeev.booktheshow.responses.QrCodeResponse;
import com.sarjeev.booktheshow.responses.TicketResponse;
import com.sarjeev.booktheshow.responses.TicketSaleResponse;
import com.sarjeev.booktheshow.responses.TicketValidationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    @Mapping(target = "attendeeId", source = "attendee.id")
    @Mapping(target = "ticketTypeId", source = "ticketType.id")
    @Mapping(target = "ticketSaleId", source = "ticketSale.id")
    @Mapping(target = "qrCodeId", source = "qrCode.id")
    TicketResponse toTicketResponse(Ticket ticket);

    @Mapping(target = "purchaserId", source = "purchaser.id")
    @Mapping(target = "eventId", source = "event.id")
    TicketSaleResponse toTicketSaleResponse(TicketSale ticketSale);

    @Mapping(target = "ticketId", source = "ticket.id")
    QrCodeResponse toQrCodeResponse(QrCode qrCode);

    @Mapping(target = "ticketId", source = "ticket.id")
    @Mapping(target = "validatedById", source = "validatedBy.id")
    TicketValidationResponse toTicketValidationResponse(TicketValidation ticketValidation);
}
