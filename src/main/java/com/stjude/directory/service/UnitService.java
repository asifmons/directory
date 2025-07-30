package com.stjude.directory.service;

import com.stjude.directory.dto.UnitCreateRequestDTO;
import com.stjude.directory.dto.UnitResponseDTO;
import com.stjude.directory.dto.UnitUpdateRequestDTO;
import com.stjude.directory.model.Unit;
import java.util.List;

public interface UnitService {
    Unit createUnit(UnitCreateRequestDTO dto);
    void updateUnitFields(String unitId, UnitUpdateRequestDTO dto);
    UnitResponseDTO getUnitById(String unitId);
    void deleteUnit(String id);
    List<UnitResponseDTO> getAllUnits();
}
