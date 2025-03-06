package com.sof.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="account")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "account_type", nullable = false, length = 100)
    private String accountType;

    @Column(name = "initial_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal initialBalance;

    @Column(nullable = false, length = 1)
    private Boolean status;

    @Column(name = "person_id", nullable = false)
    private Long personId;

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
