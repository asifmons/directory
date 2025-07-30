package com.stjude.directory.service;

import com.stjude.directory.dto.PositionDTO;
import com.stjude.directory.model.Position;
import java.util.List;

public interface PositionService {
    Position createPosition(PositionDTO positionDTO);
    List<Position> getAllPositions();
    Position updatePosition(String id, PositionDTO positionDTO);
    void deletePosition(String id);
    Position getPositionById(String id);
}
