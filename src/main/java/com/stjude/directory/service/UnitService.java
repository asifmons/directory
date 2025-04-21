package com.stjude.directory.service;

import com.stjude.directory.dto.UnitCreateRequestDTO;
import com.stjude.directory.dto.UnitExecutive;
import com.stjude.directory.dto.UnitResponseDTO;
import com.stjude.directory.dto.UnitUpdateRequestDTO;
import com.stjude.directory.model.Member;
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
    private MemberService memberService;

    @Autowired
    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public Unit createUnit(UnitCreateRequestDTO dto) {
        Unit unit = new Unit();
        unit.setNumberOfFamilies(dto.getNumberOfFamilies());
        unit.setPresident(dto.getPresident());
        unit.setVicePresident(dto.getVicePresident());
        unit.setSecretary(dto.getSecretary());
        unit.setTreasurer(dto.getTreasurer());
        unit.setJointSecretary(dto.getJointSecretary());
        unit.setJointTreasurer(dto.getJointTreasurer());
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
        Member president = memberService.getMemberById(unit.getPresident());
        UnitExecutive presidentDetail = new UnitExecutive(president.getName(), president.getFamilyId(), president.getPhoneNumber());
        Member vicePresident = memberService.getMemberById(unit.getVicePresident());
        UnitExecutive vicePresidentDetail = new UnitExecutive(vicePresident.getName(), vicePresident.getFamilyId(), vicePresident.getPhoneNumber());
        Member secretary = memberService.getMemberById(unit.getSecretary());
        UnitExecutive secretaryDetail = new UnitExecutive(secretary.getName(), secretary.getFamilyId(), secretary.getPhoneNumber());
        Member treasurer = memberService.getMemberById(unit.getTreasurer());
        UnitExecutive treasurerDetail = new UnitExecutive(treasurer.getName(), treasurer.getFamilyId(), treasurer.getPhoneNumber());
        Member jointSecretary = memberService.getMemberById(unit.getJointSecretary());
        UnitExecutive jointSecretaryDetail = new UnitExecutive(jointSecretary.getName(), jointSecretary.getFamilyId(), jointSecretary.getPhoneNumber());
        Member jointTreasurer = memberService.getMemberById(unit.getJointTreasurer());
        UnitExecutive jointTreasurerDetail = new UnitExecutive(jointTreasurer.getName(), jointTreasurer.getFamilyId(), jointTreasurer.getPhoneNumber());



        return new UnitResponseDTO(unit.getId(), unit.getNumberOfFamilies(),
                presidentDetail, vicePresidentDetail, secretaryDetail, treasurerDetail, jointSecretaryDetail, jointTreasurerDetail);
    }

    public void deleteUnit(String id) {
        unitRepository.deleteById(id);
    }

    public List<UnitResponseDTO> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(unit -> getUnitById(unit.getId()))
                .toList();
    }
}
