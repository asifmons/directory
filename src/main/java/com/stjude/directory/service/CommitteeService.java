package com.stjude.directory.service;

import com.stjude.directory.dto.CommitteeDTO;
import com.stjude.directory.model.Committee;
import java.util.List;

import com.stjude.directory.dto.CommitteeDetailDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CommitteeService {
    Committee createCommittee(CommitteeDTO committeeDTO, MultipartFile coverPhoto, MultipartFile innerCoverPhoto);
    List<Committee> getAllCommittees();
    CommitteeDetailDTO getCommitteeById(String committeeId);
    Committee updateCommittee(String committeeId, CommitteeDTO committeeDTO, MultipartFile coverPhoto, MultipartFile innerCoverPhoto);
    void deleteCommittee(String committeeId);
}
