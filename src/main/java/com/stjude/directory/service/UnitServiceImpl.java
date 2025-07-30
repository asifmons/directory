package com.stjude.directory.service;

import com.stjude.directory.dto.UnitCreateRequestDTO;
import com.stjude.directory.dto.UnitResponseDTO;
import com.stjude.directory.dto.UnitUpdateRequestDTO;
import com.stjude.directory.model.Unit;
import com.stjude.directory.repository.UnitRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public UnitServiceImpl(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    public Unit createUnit(UnitCreateRequestDTO dto) {
        Unit unit = new Unit();
        unit.setId(UUID.randomUUID().toString());
        unit.setName(dto.getName());
        unit.setNumberOfFamilies(dto.getNumberOfFamilies());
        unit.setCreatedDate(LocalDateTime.now());
        unit.setLastModifiedDate(LocalDateTime.now());
        return unitRepository.save(unit);
    }

    @Override
    public void updateUnitFields(String unitId, UnitUpdateRequestDTO dto) {
        Query query = new Query(Criteria.where("id").is(unitId));
        Update update = new Update();

        if (dto.getName() != null) {
            update.set("name", dto.getName());
        }
        if (dto.getNumberOfFamilies() != null) {
            update.set("numberOfFamilies", dto.getNumberOfFamilies());
        }
        update.set("lastModifiedDate", LocalDateTime.now());

        mongoTemplate.updateFirst(query, update, Unit.class);
    }

    @Override
    public UnitResponseDTO getUnitById(String unitId) {
        Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new RuntimeException("Unit not found"));
        return new UnitResponseDTO(unit.getId(), unit.getName(), unit.getNumberOfFamilies());
    }

    @Override
    public void deleteUnit(String id) {
        unitRepository.deleteById(id);
    }

    @Override
    public List<UnitResponseDTO> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(unit -> getUnitById(unit.getId()))
                .collect(Collectors.toList());
    }
}
