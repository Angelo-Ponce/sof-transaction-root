package com.sof.mappers;

import com.sof.dto.MovementDTO;
import com.sof.model.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovementMapper {

    MovementMapper INSTANCE = Mappers.getMapper(MovementMapper.class);

    MovementDTO toMovementDTO(Movement movement);

    Movement toMovement(MovementDTO movementDTO);
}
