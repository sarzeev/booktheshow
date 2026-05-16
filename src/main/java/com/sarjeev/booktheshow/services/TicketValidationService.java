package com.sarjeev.booktheshow.services;

import com.sarjeev.booktheshow.entities.TicketValidation;
import com.sarjeev.booktheshow.requests.TicketValidationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TicketValidationService {

    TicketValidation validateTicket(UUID eventId, TicketValidationRequest request);

    Page<TicketValidation> listValidations(UUID eventId, Pageable pageable);
}
