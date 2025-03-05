package com.sof.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="movement")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long movementId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "movement_date", nullable = false)
    private LocalDateTime movementDate;

    @Column(name = "movement_type", nullable = false, length = 150)
    private String movementType;

    @Column(name = "movement_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal movementValue;

    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, length = 1)
    private Boolean status;

    @ManyToOne(fetch = FetchType.EAGER) // FK
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", foreignKey = @ForeignKey(name = "FK_MOVEMENT_ACCOUNT"), insertable = false, updatable = false)
    private Account account;

    // Campos de auditoria
    @Column(length = 30)
    private String createdByUser;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(length = 30)
    private String lastModifiedByUser;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
