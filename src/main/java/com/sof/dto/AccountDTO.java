package com.sof.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountDTO {

    @EqualsAndHashCode.Include
    private Long accountId;

    @NotNull(message = "{account.number.empty}")
    private String accountNumber;

    @NotEmpty(message = "{account.type.empty}")
    @Size(min = 5, max = 100, message = "{account.type.size}")
    private String accountType;

    @NotNull(message = "{initial.balance.empty}")
    private BigDecimal initialBalance;

    private Boolean status;

    @NotNull(message = "{person.id.empty}")
    private Long personId;
}
