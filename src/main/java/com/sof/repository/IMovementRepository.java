package com.sof.repository;

import com.sof.model.Movement;

import java.time.LocalDateTime;
import java.util.List;

public interface IMovementRepository extends IGenericRepository<Movement, Long> {

    List<Movement> findByAccountIdAndMovementDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
}
