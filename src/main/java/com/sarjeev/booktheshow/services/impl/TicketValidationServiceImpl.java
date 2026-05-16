package com.sarjeev.booktheshow.services.impl;

import com.sarjeev.booktheshow.entities.Event;
import com.sarjeev.booktheshow.entities.QrCode;
import com.sarjeev.booktheshow.entities.Ticket;
import com.sarjeev.booktheshow.entities.TicketValidation;
import com.sarjeev.booktheshow.entities.User;
import com.sarjeev.booktheshow.enums.QrCodeStatusEnum;
import com.sarjeev.booktheshow.enums.TicketStatusEnum;
import com.sarjeev.booktheshow.enums.TicketValidationStatusEnum;
import com.sarjeev.booktheshow.enums.ValidationMethodEnum;
import com.sarjeev.booktheshow.exceptions.AccessDeniedException;
import com.sarjeev.booktheshow.exceptions.EventNotFoundException;
import com.sarjeev.booktheshow.exceptions.InvalidQrCodeException;
import com.sarjeev.booktheshow.exceptions.TicketNotFoundException;
import com.sarjeev.booktheshow.repositories.EventRepository;
import com.sarjeev.booktheshow.repositories.QrCodeRepository;
import com.sarjeev.booktheshow.repositories.TicketRepository;
import com.sarjeev.booktheshow.repositories.TicketValidationRepository;
import com.sarjeev.booktheshow.requests.TicketValidationRequest;
import com.sarjeev.booktheshow.services.TicketValidationService;
import com.sarjeev.booktheshow.utils.SecurityUtils;
import com.sarjeev.booktheshow.utils.TicketValidationReferenceParser;
import com.sarjeev.booktheshow.utils.TicketValidationReferenceParser.ParsedPayload;
import com.sarjeev.booktheshow.validators.OwnershipValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketValidationServiceImpl implements TicketValidationService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeRepository qrCodeRepository;
    private final TicketValidationRepository ticketValidationRepository;
    private final OwnershipValidator ownershipValidator;

    @Override
    @Transactional
    public TicketValidation validateTicket(UUID eventId, TicketValidationRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        validateStaffCanAccessEvent(event);

        Ticket ticket = resolveTicketReference(request.id(), request.validationMethod());

        TicketValidationStatusEnum status = determineValidationStatus(eventId, ticket);
        if (status == TicketValidationStatusEnum.SUCCESS) {
            ticket.setStatus(TicketStatusEnum.USED);
            if (ticket.getQrCode() != null) {
                ticket.getQrCode().setStatus(QrCodeStatusEnum.USED);
                qrCodeRepository.save(ticket.getQrCode());
            }
            ticketRepository.save(ticket);
        }

        TicketValidation validation = TicketValidation.builder()
                .ticket(ticket)
                .validatedBy(SecurityUtils.currentUser())
                .validationMethod(request.validationMethod())
                .status(status)
                .build();
        return ticketValidationRepository.save(validation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketValidation> listValidations(UUID eventId, Pageable pageable) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        if (!SecurityUtils.hasRole("ROLE_STAFF")) {
            ownershipValidator.validateEventOwnerOrAdmin(event, SecurityUtils.currentUser());
        }
        return ticketValidationRepository.findByTicketTicketTypeEventId(eventId, pageable);
    }

    private Ticket resolveTicketReference(String reference, ValidationMethodEnum validationMethod) {
        String trimmed = reference == null ? "" : reference.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidQrCodeException("Ticket ID, QR code ID, or scan payload is required");
        }

        Optional<ParsedPayload> payload = TicketValidationReferenceParser.parsePayload(trimmed);
        if (payload.isPresent()) {
            return resolveFromPayload(payload.get(), validationMethod);
        }

        UUID uuid = TicketValidationReferenceParser.parseUuid(trimmed)
                .orElseThrow(() -> new InvalidQrCodeException(
                        "Enter a valid ticket ID, QR code ID, or full scan payload (booktheshow:ticket:...:qr:...)"
                ));

        if (validationMethod == ValidationMethodEnum.QR_SCAN) {
            return resolveTicketByQrCode(uuid);
        }

        return resolveTicketByIdOrQrCode(uuid);
    }

    private Ticket resolveFromPayload(ParsedPayload payload, ValidationMethodEnum validationMethod) {
        if (validationMethod == ValidationMethodEnum.QR_SCAN) {
            return resolveTicketByQrCode(payload.qrCodeId());
        }

        try {
            return resolveTicketById(payload.ticketId());
        } catch (TicketNotFoundException ex) {
            return resolveTicketByQrCode(payload.qrCodeId());
        }
    }

    private Ticket resolveTicketByIdOrQrCode(UUID uuid) {
        Optional<Ticket> ticket = ticketRepository.findById(uuid);
        if (ticket.isPresent()) {
            return ticket.get();
        }
        return resolveTicketByQrCode(uuid);
    }

    private Ticket resolveTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode = qrCodeRepository.findById(qrCodeId)
                .orElseThrow(() -> new InvalidQrCodeException(
                        "No ticket found for QR code ID. Check the value or paste the full scan payload."
                ));
        if (qrCode.getStatus() == QrCodeStatusEnum.INVALID) {
            throw new InvalidQrCodeException("This QR code has been invalidated");
        }
        if (qrCode.getTicket() == null) {
            throw new InvalidQrCodeException("No ticket is linked to this QR code");
        }
        return qrCode.getTicket();
    }

    private Ticket resolveTicketById(UUID ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(
                        "No ticket found for that ID. You can also enter the QR code ID or full scan payload."
                ));
    }

    private TicketValidationStatusEnum determineValidationStatus(UUID eventId, Ticket ticket) {
        if (!ticket.getTicketType().getEvent().getId().equals(eventId)) {
            return TicketValidationStatusEnum.INVALID;
        }
        if (ticket.getStatus() == TicketStatusEnum.CANCELLED || ticket.getStatus() == TicketStatusEnum.EXPIRED) {
            return TicketValidationStatusEnum.INVALID;
        }
        if (ticket.getStatus() == TicketStatusEnum.USED) {
            return TicketValidationStatusEnum.DUPLICATE;
        }
        boolean alreadySuccessfullyValidated = ticketValidationRepository.existsByTicketIdAndStatus(
                ticket.getId(),
                TicketValidationStatusEnum.SUCCESS
        );
        if (alreadySuccessfullyValidated) {
            return TicketValidationStatusEnum.DUPLICATE;
        }
        return TicketValidationStatusEnum.SUCCESS;
    }

    private void validateStaffCanAccessEvent(Event event) {
        User currentUser = SecurityUtils.currentUser();
        if (SecurityUtils.isAdmin() || SecurityUtils.hasRole("ROLE_STAFF")) {
            return;
        }
        if (SecurityUtils.hasRole("ROLE_ORGANIZER")) {
            ownershipValidator.validateEventOwnerOrAdmin(event, currentUser);
            return;
        }
        throw new AccessDeniedException("Only staff, organizers, or admins can access ticket validation");
    }
}
