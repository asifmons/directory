package com.stjude.directory.service;

import com.stjude.directory.dto.CommitteeDTO;
import com.stjude.directory.model.Committee;
import java.util.List;

import com.stjude.directory.dto.CommitteeDetailDTO;

public interface CommitteeService {
    Committee createCommittee(CommitteeDTO committeeDTO);
    List<Committee> getAllCommittees();
    CommitteeDetailDTO getCommitteeById(String committeeId);
    Committee updateCommittee(String committeeId, CommitteeDTO committeeDTO);
    void deleteCommittee(String committeeId);
}
