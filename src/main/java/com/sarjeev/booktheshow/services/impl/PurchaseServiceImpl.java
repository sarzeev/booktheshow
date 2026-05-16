package com.sarjeev.booktheshow.services.impl;

import com.sarjeev.booktheshow.entities.Event;
import com.sarjeev.booktheshow.entities.QrCode;
import com.sarjeev.booktheshow.entities.Ticket;
import com.sarjeev.booktheshow.entities.TicketSale;
import com.sarjeev.booktheshow.entities.TicketType;
import com.sarjeev.booktheshow.entities.User;
import com.sarjeev.booktheshow.enums.EventStatusEnum;
import com.sarjeev.booktheshow.enums.TicketSaleStatusEnum;
import com.sarjeev.booktheshow.enums.TicketStatusEnum;
import com.sarjeev.booktheshow.exceptions.EventNotFoundException;
import com.sarjeev.booktheshow.exceptions.TicketTypeNotFoundException;
import com.sarjeev.booktheshow.repositories.EventRepository;
import com.sarjeev.booktheshow.repositories.TicketRepository;
import com.sarjeev.booktheshow.repositories.TicketSaleRepository;
import com.sarjeev.booktheshow.repositories.TicketTypeRepository;
import com.sarjeev.booktheshow.responses.PurchaseResponse;
import com.sarjeev.booktheshow.services.PurchaseService;
import com.sarjeev.booktheshow.services.QrCodeService;
import com.sarjeev.booktheshow.utils.SecurityUtils;
import com.sarjeev.booktheshow.validators.PurchaseValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketSaleRepository ticketSaleRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;
    private final PurchaseValidator purchaseValidator;

    @Override
    @Transactional
    public PurchaseResponse purchaseTicket(UUID eventId, UUID ticketTypeId) {
        User attendee = SecurityUtils.currentUser();
        Event event = eventRepository.findByIdAndStatus(eventId, EventStatusEnum.PUBLISHED)
                .orElseThrow(() -> new EventNotFoundException("Published event not found"));
        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId)
                .orElseThrow(() -> new TicketTypeNotFoundException("Ticket type not found"));

        purchaseValidator.validateTicketCanBePurchased(event, ticketType);

        ticketType.setRemainingTickets(ticketType.getRemainingTickets() - 1);
        ticketTypeRepository.save(ticketType);

        TicketSale sale = TicketSale.builder()
                .amount(ticketType.getPrice())
                .status(TicketSaleStatusEnum.COMPLETED)
                .purchaser(attendee)
                .event(event)
                .build();
        TicketSale savedSale = ticketSaleRepository.save(sale);

        Ticket ticket = Ticket.builder()
                .status(TicketStatusEnum.ACTIVE)
                .attendee(attendee)
                .ticketType(ticketType)
                .ticketSale(savedSale)
                .build();
        Ticket savedTicket = ticketRepository.save(ticket);
        QrCode qrCode = qrCodeService.generateQrCode(savedTicket);

        return new PurchaseResponse(
                savedSale.getId(),
                savedTicket.getId(),
                qrCode.getId(),
                event.getId(),
                ticketType.getId(),
                savedSale.getAmount(),
                savedSale.getStatus(),
                savedTicket.getStatus(),
                savedSale.getPurchaseDateTime()
        );
    }
}
