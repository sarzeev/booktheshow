package com.sarjeev.booktheshow.validators;

import com.sarjeev.booktheshow.enums.EventStatusEnum;
import com.sarjeev.booktheshow.exceptions.BookTheShowException;
import com.sarjeev.booktheshow.exceptions.InvalidEventStateException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventScheduleValidator {

    public void validate(LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime salesEndDate) {
        if (startDateTime == null || endDateTime == null || salesEndDate == null) {
            throw new BookTheShowException("Event date fields are required");
        }
        if (!endDateTime.isAfter(startDateTime)) {
            throw new BookTheShowException("Event end date must be after the start date");
        }
        if (salesEndDate.isAfter(startDateTime)) {
            throw new BookTheShowException("Ticket sales must end before the event starts");
        }
    }

    public void validateStateTransition(EventStatusEnum currentStatus, EventStatusEnum nextStatus) {
        if (currentStatus == null || nextStatus == null) {
            throw new InvalidEventStateException("Event status is required");
        }
        if (currentStatus == EventStatusEnum.COMPLETED && nextStatus != EventStatusEnum.COMPLETED) {
            throw new InvalidEventStateException("Completed events cannot transition to another state");
        }
        if (currentStatus == EventStatusEnum.CANCELLED && nextStatus != EventStatusEnum.CANCELLED) {
            throw new InvalidEventStateException("Cancelled events cannot transition to another state");
        }
        if (currentStatus == EventStatusEnum.PUBLISHED && nextStatus == EventStatusEnum.DRAFT) {
            throw new InvalidEventStateException("Published events cannot move back to draft");
        }
    }
}
