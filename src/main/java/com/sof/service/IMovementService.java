package com.sof.service;

import com.sof.dto.MovementReportDTO;
import com.sof.model.Movement;

import java.time.LocalDateTime;
import java.util.List;

public interface IMovementService extends ICRUDService<Movement, Long> {

    Movement saveMovement(Movement movement, String user);
    Movement updateMovement(Long id, Movement movement, String user);
    void deleteLogic(Long id, String user);

    List<MovementReportDTO> reportMovementByDateAndClientId(String clientId, LocalDateTime startDate, LocalDateTime endDate);
}
