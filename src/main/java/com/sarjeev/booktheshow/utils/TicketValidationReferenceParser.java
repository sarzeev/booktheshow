package com.sarjeev.booktheshow.utils;

import java.util.Optional;
import java.util.UUID;

public final class TicketValidationReferenceParser {

    private static final String TICKET_PREFIX = "booktheshow:ticket:";
    private static final String QR_SEGMENT = ":qr:";

    private TicketValidationReferenceParser() {
    }

    public record ParsedPayload(UUID ticketId, UUID qrCodeId) {
    }

    public static Optional<ParsedPayload> parsePayload(String reference) {
        String trimmed = reference == null ? "" : reference.trim();
        if (!trimmed.startsWith(TICKET_PREFIX) || !trimmed.contains(QR_SEGMENT)) {
            return Optional.empty();
        }

        int qrIndex = trimmed.indexOf(QR_SEGMENT);
        String ticketPart = trimmed.substring(TICKET_PREFIX.length(), qrIndex);
        String qrPart = trimmed.substring(qrIndex + QR_SEGMENT.length());

        try {
            return Optional.of(new ParsedPayload(UUID.fromString(ticketPart), UUID.fromString(qrPart)));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    public static Optional<UUID> parseUuid(String reference) {
        String trimmed = reference == null ? "" : reference.trim();
        if (trimmed.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(trimmed));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
