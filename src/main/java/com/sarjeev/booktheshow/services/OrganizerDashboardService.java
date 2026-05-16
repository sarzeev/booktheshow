package com.sarjeev.booktheshow.services;

import com.sarjeev.booktheshow.responses.DashboardSummaryResponse;
import com.sarjeev.booktheshow.responses.SalesReportResponse;
import com.sarjeev.booktheshow.responses.ValidationReportResponse;

import java.util.UUID;

public interface OrganizerDashboardService {

    DashboardSummaryResponse getDashboardSummary(UUID eventId);

    SalesReportResponse getSalesReport(UUID eventId);

    ValidationReportResponse getValidationReport(UUID eventId);
}
