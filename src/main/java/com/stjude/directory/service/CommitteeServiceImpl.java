package com.stjude.directory.service;

import com.stjude.directory.dto.CommitteeCardDetailDTO;
import com.stjude.directory.dto.CommitteeDTO;
import com.stjude.directory.dto.CommitteeDetailDTO;
import com.stjude.directory.dto.PositionMemberDetailDTO;
import com.stjude.directory.model.Committee;
import com.stjude.directory.model.CommitteeCard;
import com.stjude.directory.model.CommitteePosition;
import com.stjude.directory.model.Member;
import com.stjude.directory.model.Position;
import com.stjude.directory.repository.CommitteeRepository;
import com.stjude.directory.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CommitteeServiceImpl implements CommitteeService {

    @Autowired
    private CommitteeRepository committeeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PositionService positionService;

    @Autowired
    private S3Service s3Service;

    @Override
    public Committee createCommittee(CommitteeDTO committeeDTO, MultipartFile coverPhoto, MultipartFile innerCoverPhoto) {
        Committee committee = new Committee();
        committee.setName(committeeDTO.getName());

        List<CommitteeCard> cards = committeeDTO.getCards().stream().map(cardDTO -> {
            CommitteeCard card = new CommitteeCard();
            card.setTitle(cardDTO.getTitle());
            List<CommitteePosition> positions = cardDTO.getPositions().stream().map(posDTO -> {
                CommitteePosition position = new CommitteePosition();
                position.setPositionId(posDTO.getPositionId());
                Position pos = positionService.getPositionById(posDTO.getPositionId());
                position.setPositionName(pos != null ? pos.getName() : null);
                position.setMemberId(posDTO.getMemberId());
                Member member = memberRepository.findById(posDTO.getMemberId()).orElse(null);
                position.setMemberName(member != null ? member.getName() : null);
                return position;
            }).collect(Collectors.toList());
            card.setPositions(positions);
            return card;
        }).collect(Collectors.toList());

        committee.setCards(cards);
        committee.setCreatedDate(LocalDateTime.now());
        committee.setLastModifiedDate(LocalDateTime.now());

        if (committeeDTO.isRemoveCoverPhoto()) {
            //s3Service.deleteFile("committee-photos", "cover-photo-" + committee.getId());
            committee.setCoverPhotoUrl(null);
        } else if (coverPhoto != null) {
            String coverPhotoUrl = s3Service.uploadFile(coverPhoto, "committee-photos", "cover-photo-" + committee.getId());
            committee.setCoverPhotoUrl(coverPhotoUrl);
        }

        if (committeeDTO.isRemoveInnerCoverPhoto()) {
            //s3Service.deleteFile("committee-photos", "inner-photo-" + committee.getId());
            committee.setInnerCoverPhotoUrl(null);
        } else if (innerCoverPhoto != null) {
            String innerCoverPhotoUrl = s3Service.uploadFile(innerCoverPhoto, "committee-photos", "inner-photo-" + committee.getId());
            committee.setInnerCoverPhotoUrl(innerCoverPhotoUrl);
        }

        return committeeRepository.save(committee);
    }

    @Override
    public List<Committee> getAllCommittees() {
        return committeeRepository.findAll();
    }

    @Override
    public CommitteeDetailDTO getCommitteeById(String committeeId) {
        Committee committee = committeeRepository.findById(committeeId)
                .orElseThrow(() -> new RuntimeException("Committee not found"));

        List<CommitteeCardDetailDTO> cardDetails = committee.getCards().stream().map(card -> {
            List<PositionMemberDetailDTO> positionMemberDetails = card.getPositions().stream().map(position -> {
                return PositionMemberDetailDTO.builder()
                        .positionName(position.getPositionName())
                        .positionId(position.getPositionId())
                        .memberId(position.getMemberId())
                        .memberName(position.getMemberName())
                        .build();
            }).collect(Collectors.toList());
            return CommitteeCardDetailDTO.builder()
                    .title(card.getTitle())
                    .positions(positionMemberDetails)
                    .build();
        }).collect(Collectors.toList());

        return CommitteeDetailDTO.builder()
                .id(committee.getId())
                .name(committee.getName())
                .cards(cardDetails)
                .build();
    }

    @Override
    public Committee updateCommittee(String committeeId, CommitteeDTO committeeDTO, MultipartFile coverPhoto, MultipartFile innerCoverPhoto) {
        Committee committee = committeeRepository.findById(committeeId)
                .orElseThrow(() -> new RuntimeException("Committee not found"));

        committee.setName(committeeDTO.getName());

        List<CommitteeCard> cards = committeeDTO.getCards().stream().map(cardDTO -> {
            CommitteeCard card = new CommitteeCard();
            card.setTitle(cardDTO.getTitle());
            List<CommitteePosition> positions = cardDTO.getPositions().stream().map(posDTO -> {
                CommitteePosition position = new CommitteePosition();
                position.setPositionId(posDTO.getPositionId());
                Position pos = positionService.getPositionById(posDTO.getPositionId());
                position.setPositionName(pos != null ? pos.getName() : null);
                position.setMemberId(posDTO.getMemberId());
                Member member = memberRepository.findById(posDTO.getMemberId()).orElse(null);
                position.setMemberName(member != null ? member.getName() : null);
                return position;
            }).collect(Collectors.toList());
            card.setPositions(positions);
            return card;
        }).collect(Collectors.toList());

        committee.setCards(cards);
        committee.setLastModifiedDate(LocalDateTime.now());

        if (coverPhoto != null) {
            String coverPhotoUrl = s3Service.uploadFile(coverPhoto, "committee-photos", "cover-photo-" + committee.getId());
            committee.setCoverPhotoUrl(coverPhotoUrl);
        }

        if (innerCoverPhoto != null) {
            String innerCoverPhotoUrl = s3Service.uploadFile(innerCoverPhoto, "committee-photos", "inner-photo-" + committee.getId());
            committee.setInnerCoverPhotoUrl(innerCoverPhotoUrl);
        }

        return committeeRepository.save(committee);
    }

    @Override
    public void deleteCommittee(String committeeId) {
        committeeRepository.deleteById(committeeId);
    }
}
