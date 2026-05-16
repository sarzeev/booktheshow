package com.sarjeev.booktheshow.services.impl;

import com.sarjeev.booktheshow.entities.QrCode;
import com.sarjeev.booktheshow.entities.Ticket;
import com.sarjeev.booktheshow.enums.QrCodeStatusEnum;
import com.sarjeev.booktheshow.exceptions.QrCodeNotFoundException;
import com.sarjeev.booktheshow.repositories.QrCodeRepository;
import com.sarjeev.booktheshow.services.QrCodeService;
import com.sarjeev.booktheshow.utils.QrCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final QrCodeGenerator qrCodeGenerator;

    @Override
    @Transactional
    public QrCode generateQrCode(Ticket ticket) {
        QrCode qrCode = QrCode.builder()
                .qrCodeData("PENDING")
                .status(QrCodeStatusEnum.ACTIVE)
                .ticket(ticket)
                .build();
        QrCode savedQrCode = qrCodeRepository.saveAndFlush(qrCode);
        String payload = buildQrPayload(savedQrCode);
        savedQrCode.setQrCodeData(qrCodeGenerator.generateBase64Png(payload));
        ticket.setQrCode(savedQrCode);
        return qrCodeRepository.save(savedQrCode);
    }

    @Override
    @Transactional(readOnly = true)
    public QrCode getQrCodeForTicket(UUID ticketId) {
        return qrCodeRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new QrCodeNotFoundException("QR code not found for ticket"));
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getQrCodeImageForTicket(UUID ticketId) {
        QrCode qrCode = getQrCodeForTicket(ticketId);
        return qrCodeGenerator.decodeBase64Image(qrCode.getQrCodeData());
    }

    private String buildQrPayload(QrCode qrCode) {
        return "booktheshow:ticket:%s:qr:%s".formatted(qrCode.getTicket().getId(), qrCode.getId());
    }
}
