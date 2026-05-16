package com.sarjeev.booktheshow.repositories;

import com.sarjeev.booktheshow.entities.TicketSale;
import com.sarjeev.booktheshow.enums.TicketSaleStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface TicketSaleRepository extends JpaRepository<TicketSale, UUID> {

    Page<TicketSale> findByPurchaserId(UUID purchaserId, Pageable pageable);

    Page<TicketSale> findByEventId(UUID eventId, Pageable pageable);

    Page<TicketSale> findByEventIdAndStatus(UUID eventId, TicketSaleStatusEnum status, Pageable pageable);

    long countByEventId(UUID eventId);

    long countByEventIdAndStatus(UUID eventId, TicketSaleStatusEnum status);

    @Query("select coalesce(sum(ticketSale.amount), 0) from TicketSale ticketSale where ticketSale.event.id = :eventId and ticketSale.status = :status")
    BigDecimal sumAmountByEventIdAndStatus(@Param("eventId") UUID eventId, @Param("status") TicketSaleStatusEnum status);
}
