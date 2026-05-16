package com.sarjeev.booktheshow.controllers;

import com.sarjeev.booktheshow.responses.ApiResponse;
import com.sarjeev.booktheshow.responses.DashboardSummaryResponse;
import com.sarjeev.booktheshow.responses.SalesReportResponse;
import com.sarjeev.booktheshow.responses.ValidationReportResponse;
import com.sarjeev.booktheshow.services.OrganizerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events/{eventId}/dashboard")
@PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
public class OrganizerDashboardController {

    private final OrganizerDashboardService organizerDashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getDashboardSummary(@PathVariable UUID eventId) {
        DashboardSummaryResponse response = organizerDashboardService.getDashboardSummary(eventId);
        return ResponseEntity.ok(ApiResponse.success("Dashboard summary retrieved successfully", response));
    }

    @GetMapping("/sales-report")
    public ResponseEntity<ApiResponse<SalesReportResponse>> getSalesReport(@PathVariable UUID eventId) {
        SalesReportResponse response = organizerDashboardService.getSalesReport(eventId);
        return ResponseEntity.ok(ApiResponse.success("Sales report retrieved successfully", response));
    }

    @GetMapping("/validation-report")
    public ResponseEntity<ApiResponse<ValidationReportResponse>> getValidationReport(@PathVariable UUID eventId) {
        ValidationReportResponse response = organizerDashboardService.getValidationReport(eventId);
        return ResponseEntity.ok(ApiResponse.success("Validation report retrieved successfully", response));
    }
}
