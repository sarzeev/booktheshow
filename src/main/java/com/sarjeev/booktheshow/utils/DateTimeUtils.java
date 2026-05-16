package com.sarjeev.booktheshow.utils;

import java.time.Clock;
import java.time.LocalDateTime;

public final class DateTimeUtils {

    private static final Clock UTC_CLOCK = Clock.systemUTC();

    private DateTimeUtils() {
    }

    public static LocalDateTime utcNow() {
        return LocalDateTime.now(UTC_CLOCK);
    }
}
