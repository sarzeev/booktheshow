package com.sarjeev.booktheshow.services;

import com.sarjeev.booktheshow.entities.QrCode;
import com.sarjeev.booktheshow.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);

    QrCode getQrCodeForTicket(UUID ticketId);

    byte[] getQrCodeImageForTicket(UUID ticketId);
}
