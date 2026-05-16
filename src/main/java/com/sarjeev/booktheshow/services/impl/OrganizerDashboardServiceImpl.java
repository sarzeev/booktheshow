package com.sarjeev.booktheshow.services.impl;

import com.sarjeev.booktheshow.entities.Event;
import com.sarjeev.booktheshow.entities.TicketType;
import com.sarjeev.booktheshow.enums.TicketSaleStatusEnum;
import com.sarjeev.booktheshow.enums.TicketStatusEnum;
import com.sarjeev.booktheshow.enums.TicketValidationStatusEnum;
import com.sarjeev.booktheshow.exceptions.EventNotFoundException;
import com.sarjeev.booktheshow.repositories.EventRepository;
import com.sarjeev.booktheshow.repositories.TicketRepository;
import com.sarjeev.booktheshow.repositories.TicketSaleRepository;
import com.sarjeev.booktheshow.repositories.TicketValidationRepository;
import com.sarjeev.booktheshow.responses.DashboardSummaryResponse;
import com.sarjeev.booktheshow.responses.SalesReportResponse;
import com.sarjeev.booktheshow.responses.TicketInventoryResponse;
import com.sarjeev.booktheshow.responses.ValidationReportResponse;
import com.sarjeev.booktheshow.services.OrganizerDashboardService;
import com.sarjeev.booktheshow.utils.SecurityUtils;
import com.sarjeev.booktheshow.validators.OwnershipValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizerDashboardServiceImpl implements OrganizerDashboardService {

    private final EventRepository eventRepository;
    private final TicketSaleRepository ticketSaleRepository;
    private final TicketRepository ticketRepository;
    private final TicketValidationRepository ticketValidationRepository;
    private final OwnershipValidator ownershipValidator;

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryResponse getDashboardSummary(UUID eventId) {
        Event event = loadOwnedEvent(eventId);
        BigDecimal revenue = ticketSaleRepository.sumAmountByEventIdAndStatus(eventId, TicketSaleStatusEnum.COMPLETED);
        long completedSales = ticketSaleRepository.countByEventIdAndStatus(eventId, TicketSaleStatusEnum.COMPLETED);
        long ticketsSold = ticketRepository.countByTicketTypeEventId(eventId);
        long activeTickets = ticketRepository.countByTicketTypeEventIdAndStatus(eventId, TicketStatusEnum.ACTIVE);
        long usedTickets = ticketRepository.countByTicketTypeEventIdAndStatus(eventId, TicketStatusEnum.USED);
        long validationAttempts = ticketValidationRepository.countByTicketTicketTypeEventId(eventId);
        long successfulValidations = ticketValidationRepository.countByTicketTicketTypeEventIdAndStatus(eventId, TicketValidationStatusEnum.SUCCESS);
        long duplicateValidations = ticketValidationRepository.countByTicketTicketTypeEventIdAndStatus(eventId, TicketValidationStatusEnum.DUPLICATE);

        return new DashboardSummaryResponse(
                event.getId(),
                event.getName(),
                revenue,
                completedSales,
                ticketsSold,
                activeTickets,
                usedTickets,
                validationAttempts,
                successfulValidations,
                duplicateValidations,
                buildInventory(event)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SalesReportResponse getSalesReport(UUID eventId) {
        Event event = loadOwnedEvent(eventId);
        return new SalesReportResponse(
                event.getId(),
                event.getName(),
                ticketSaleRepository.sumAmountByEventIdAndStatus(eventId, TicketSaleStatusEnum.COMPLETED),
                ticketSaleRepository.countByEventIdAndStatus(eventId, TicketSaleStatusEnum.PENDING),
                ticketSaleRepository.countByEventIdAndStatus(eventId, TicketSaleStatusEnum.COMPLETED),
                ticketSaleRepository.countByEventIdAndStatus(eventId, TicketSaleStatusEnum.FAILED),
                ticketSaleRepository.countByEventIdAndStatus(eventId, TicketSaleStatusEnum.REFUNDED),
                ticketRepository.countByTicketTypeEventId(eventId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ValidationReportResponse getValidationReport(UUID eventId) {
        Event event = loadOwnedEvent(eventId);
        return new ValidationReportResponse(
                event.getId(),
                event.getName(),
                ticketValidationRepository.countByTicketTicketTypeEventId(eventId),
                ticketValidationRepository.countByTicketTicketTypeEventIdAndStatus(eventId, TicketValidationStatusEnum.SUCCESS),
                ticketValidationRepository.countByTicketTicketTypeEventIdAndStatus(eventId, TicketValidationStatusEnum.FAILED),
                ticketValidationRepository.countByTicketTicketTypeEventIdAndStatus(eventId, TicketValidationStatusEnum.DUPLICATE),
                ticketValidationRepository.countByTicketTicketTypeEventIdAndStatus(eventId, TicketValidationStatusEnum.INVALID)
        );
    }

    private Event loadOwnedEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        ownershipValidator.validateEventOwnerOrAdmin(event, SecurityUtils.currentUser());
        return event;
    }

    private List<TicketInventoryResponse> buildInventory(Event event) {
        return event.getTicketTypes().stream()
                .map(this::toInventoryResponse)
                .toList();
    }

    private TicketInventoryResponse toInventoryResponse(TicketType ticketType) {
        return new TicketInventoryResponse(
                ticketType.getId(),
                ticketType.getName(),
                ticketType.getPrice(),
                ticketType.getTotalAvailable(),
                ticketType.getRemainingTickets(),
                ticketType.getTotalAvailable() - ticketType.getRemainingTickets()
        );
    }
}
