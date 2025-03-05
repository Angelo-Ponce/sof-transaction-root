package com.sof.service;

import com.sof.model.Movement;

public interface IMovementService extends ICRUDService<Movement, Long> {

    Movement saveMovement(Movement movement);
    Movement updateMovement(Movement movement);
    void deleteLogic(Long id, String user);
}
