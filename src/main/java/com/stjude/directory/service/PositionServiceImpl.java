package com.stjude.directory.service;

import com.stjude.directory.dto.PositionDTO;
import com.stjude.directory.model.Position;
import com.stjude.directory.repository.PositionRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Override
    public Position createPosition(PositionDTO positionDTO) {
        Position position = new Position();
        position.setName(positionDTO.getName());
        position.setCreatedDate(LocalDateTime.now());
        position.setLastModifiedDate(LocalDateTime.now());
        return positionRepository.save(position);
    }

    @Override
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    @Override
    public Position updatePosition(String id, PositionDTO positionDTO) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        position.setName(positionDTO.getName());
        position.setLastModifiedDate(LocalDateTime.now());
        return positionRepository.save(position);
    }

    @Override
    public void deletePosition(String id) {
        positionRepository.deleteById(id);
    }

    @Override
    public Position getPositionById(String id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
    }
}
