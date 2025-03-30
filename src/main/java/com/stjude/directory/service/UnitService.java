package com.stjude.directory.service;

import com.stjude.directory.dto.UnitCreateRequestDTO;
import com.stjude.directory.dto.UnitResponseDTO;
import com.stjude.directory.dto.UnitUpdateRequestDTO;
import com.stjude.directory.model.Unit;
import com.stjude.directory.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public  class UnitService {
    private final UnitRepository unitRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public Unit createUnit(UnitCreateRequestDTO dto) {
        Unit unit = new Unit(dto.getNumberOfFamilies(), dto.getPresident(), dto.getVicePresident(), dto.getSecretary(), dto.getTreasurer());
        unit.setId(UUID.randomUUID().toString());
        return unitRepository.save(unit);
    }

    public void updateUnitFields(String unitId, UnitUpdateRequestDTO dto) {
        Query query = new Query(Criteria.where("id").is(unitId));
        Update update = new Update();

        if (dto.getNumberOfFamilies() != null) {
            update.set("numberOfFamilies", dto.getNumberOfFamilies());
        }
        if (dto.getPresident() != null) {
            update.set("president", dto.getPresident());
        }
        if (dto.getVicePresident() != null) {
            update.set("vicePresident", dto.getVicePresident());
        }
        if (dto.getSecretary() != null) {
            update.set("secretary", dto.getSecretary());
        }
        if (dto.getTreasurer() != null) {
            update.set("treasurer", dto.getTreasurer());
        }

        mongoTemplate.updateFirst(query, update, Unit.class);
    }

    public UnitResponseDTO getUnitById(String unitId) {
        Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new RuntimeException("Unit not found"));
        return new UnitResponseDTO(unit.getId(), unit.getNumberOfFamilies(),
                unit.getPresident(), unit.getVicePresident(),
                unit.getSecretary(), unit.getTreasurer());
    }

    public void deleteUnit(String id) {
        unitRepository.deleteById(id);
    }

    public List<UnitResponseDTO> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(unit -> new UnitResponseDTO(unit.getId(), unit.getNumberOfFamilies(),
                        unit.getPresident(), unit.getVicePresident(),
                        unit.getSecretary(), unit.getTreasurer()))
                .toList();
    }
}
