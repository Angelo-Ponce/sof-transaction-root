package com.sof.repository;

import com.sof.model.Movement;

import java.time.LocalDateTime;
import java.util.List;

public interface IMovementRepository extends IGenericRepository<Movement, Long> {

//    @Query("select m FROM movement m where m.account.personId = :accountPersonId and m.movementDate between :startDate and :endDate")
//    List<Movement> reportMovement(@Param("accountPersonId") String accountPersonId,
//                                        @Param("startDate") LocalDateTime startDate,
//                                        @Param("endDate") LocalDateTime endDate);


    List<Movement> findMovementByAccount_PersonIdAndMovementDateBetween(Long accountPersonId, LocalDateTime movementDateAfter, LocalDateTime movementDateBefore);
}
