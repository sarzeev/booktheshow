package com.sarjeev.booktheshow.services;

import com.sarjeev.booktheshow.responses.PurchaseResponse;

import java.util.UUID;

public interface PurchaseService {

    PurchaseResponse purchaseTicket(UUID eventId, UUID ticketTypeId);
}
