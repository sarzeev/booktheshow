package com.sarjeev.booktheshow.services.impl;

import com.sarjeev.booktheshow.entities.Ticket;
import com.sarjeev.booktheshow.exceptions.TicketNotFoundException;
import com.sarjeev.booktheshow.repositories.TicketRepository;
import com.sarjeev.booktheshow.services.TicketService;
import com.sarjeev.booktheshow.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Ticket> listTicketsForCurrentAttendee(Pageable pageable) {
        if (SecurityUtils.isAdmin()) {
            return ticketRepository.findAll(pageable);
        }
        return ticketRepository.findByAttendeeId(SecurityUtils.currentUserId(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket getTicketForCurrentAttendee(UUID ticketId) {
        if (SecurityUtils.isAdmin()) {
            return ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
        }
        return ticketRepository.findByIdAndAttendeeId(ticketId, SecurityUtils.currentUserId())
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
    }
}
