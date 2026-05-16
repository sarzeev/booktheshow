package com.sarjeev.booktheshow.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sarjeev.booktheshow.enums.TicketValidationStatusEnum;
import com.sarjeev.booktheshow.enums.ValidationMethodEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "ticket_validations",
        indexes = {
                @Index(name = "idx_ticket_validations_ticket", columnList = "ticket_id"),
                @Index(name = "idx_ticket_validations_validated_by", columnList = "validated_by_id"),
                @Index(name = "idx_ticket_validations_status", columnList = "status")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketValidation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private TicketValidationStatusEnum status;

    @CreatedDate
    @Column(name = "validation_time", nullable = false, updatable = false)
    private LocalDateTime validationTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "validation_method", nullable = false, length = 32)
    private ValidationMethodEnum validationMethod;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "validated_by_id", nullable = false)
    private User validatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketValidation that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
