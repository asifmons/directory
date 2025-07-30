package com.stjude.directory.service;

import com.stjude.directory.dto.AssignRepresentativesRequestDTO;
import com.stjude.directory.dto.CardDetailDTO;
import com.stjude.directory.dto.PositionMemberDetailDTO;
import com.stjude.directory.dto.UnitRepresentativesDetailDTO;
import com.stjude.directory.dto.UnitRepresentativesTemplateDTO;
import com.stjude.directory.model.Member;
import com.stjude.directory.model.Position;
import com.stjude.directory.model.PositionReference;
import com.stjude.directory.model.RepresentativeCard;
import com.stjude.directory.model.UnitRepresentatives;
import com.stjude.directory.model.UnitRepresentativesTemplate;
import com.stjude.directory.repository.MemberRepository;
import com.stjude.directory.repository.UnitRepresentativesRepository;
import com.stjude.directory.repository.UnitRepresentativesTemplateRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UnitRepresentativesServiceImpl implements UnitRepresentativesService {

    @Autowired
    private UnitRepresentativesTemplateRepository templateRepository;

    @Autowired
    private UnitRepresentativesRepository representativesRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PositionService positionService;

    @Autowired
    private S3Service s3Service;

    @Override
    public UnitRepresentativesTemplate createTemplate(UnitRepresentativesTemplateDTO templateDTO) {
        UnitRepresentativesTemplate template = new UnitRepresentativesTemplate();
        template.setName(templateDTO.getName());

        List<RepresentativeCard> cards = templateDTO.getCards().stream().map(cardDTO -> {
            RepresentativeCard card = new RepresentativeCard();
            card.setId(UUID.randomUUID().toString());
            card.setTitle(cardDTO.getTitle());
            List<PositionReference> positions = cardDTO.getPositions().stream().map(posRefDTO -> {
                Position position = positionService.getPositionById(posRefDTO.getId());
                return new PositionReference(position.getId(), position.getName());
            }).collect(Collectors.toList());
            card.setPositions(positions);
            return card;
        }).collect(Collectors.toList());

        template.setCards(cards);
        template.setCreatedDate(LocalDateTime.now());
        template.setLastModifiedDate(LocalDateTime.now());
        return templateRepository.save(template);
    }

    @Override
    public List<UnitRepresentativesTemplate> getAllTemplates() {
        return templateRepository.findAll();
    }

    @Override
    public UnitRepresentatives assignRepresentatives(AssignRepresentativesRequestDTO requestDTO, MultipartFile coverPhoto, MultipartFile innerCoverPhoto) {
        UnitRepresentatives representatives = representativesRepository.findByUnitId(requestDTO.getUnitId())
                .orElse(new UnitRepresentatives());
        if (representatives.getCreatedDate() == null) {
            representatives.setCreatedDate(LocalDateTime.now());
        }
        representatives.setUnitId(requestDTO.getUnitId());
        representatives.setTemplateId(requestDTO.getTemplateId());
        representatives.setCardPositionMemberMap(requestDTO.getCardPositionMemberMap());

        if (coverPhoto != null) {
            String coverPhotoUrl = s3Service.uploadFile(coverPhoto, "unit-photos", "cover-photo-" + requestDTO.getUnitId());
            representatives.setCoverPhotoUrl(coverPhotoUrl);
        }

        if (innerCoverPhoto != null) {
            String innerCoverPhotoUrl = s3Service.uploadFile(innerCoverPhoto, "unit-photos", "inner-photo-" + requestDTO.getUnitId());
            representatives.setInnerCoverPhotoUrl(innerCoverPhotoUrl);
        }

        representatives.setLastModifiedDate(LocalDateTime.now());
        return representativesRepository.save(representatives);
    }

    @Override
    public UnitRepresentativesDetailDTO getRepresentativesByUnit(String unitId) {
        UnitRepresentatives representatives = representativesRepository.findByUnitId(unitId)
                .orElseThrow(() -> new RuntimeException("Unit representatives not found"));
        UnitRepresentativesTemplate template = templateRepository.findById(representatives.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        List<CardDetailDTO> cardDetails = template.getCards().stream().map(card -> {
            List<PositionMemberDetailDTO> positionMemberDetails = card.getPositions().stream().map(position -> {
                String memberId = representatives.getCardPositionMemberMap().get(card.getId()).get(position.getId());
                Member member = memberRepository.findById(memberId).orElse(null);
                return PositionMemberDetailDTO.builder()
                        .positionName(position.getName())
                        .positionId(position.getId())
                        .memberId(member != null ? member.getId() : null)
                        .memberName(member != null ? member.getName() : null)
                        .build();
            }).collect(Collectors.toList());
            return CardDetailDTO.builder()
                    .title(card.getTitle())
                    .positions(positionMemberDetails)
                    .build();
        }).collect(Collectors.toList());

        return UnitRepresentativesDetailDTO.builder()
                .coverPhotoUrl(representatives.getCoverPhotoUrl())
                .innerCoverPhotoUrl(representatives.getInnerCoverPhotoUrl())
                .cards(cardDetails)
                .build();
    }

    @Override
    public UnitRepresentativesTemplate updateTemplate(String templateId, UnitRepresentativesTemplateDTO templateDTO) {
        UnitRepresentativesTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        template.setName(templateDTO.getName());
        List<RepresentativeCard> cards = templateDTO.getCards().stream().map(cardDTO -> {
            RepresentativeCard card = new RepresentativeCard();
            card.setId(cardDTO.getId() != null ? cardDTO.getId() : UUID.randomUUID().toString());
            card.setTitle(cardDTO.getTitle());
            List<PositionReference> positions = cardDTO.getPositions().stream().map(posRefDTO -> {
                Position position = positionService.getPositionById(posRefDTO.getId());
                return new PositionReference(position.getId(), position.getName());
            }).collect(Collectors.toList());
            card.setPositions(positions);
            return card;
        }).collect(Collectors.toList());
        template.setCards(cards);
        template.setLastModifiedDate(LocalDateTime.now());
        return templateRepository.save(template);
    }

    @Override
    public UnitRepresentatives updateAssignedRepresentatives(String unitId, AssignRepresentativesRequestDTO requestDTO, MultipartFile coverPhoto, MultipartFile innerCoverPhoto) {
        UnitRepresentatives representatives = representativesRepository.findByUnitId(unitId)
                .orElseThrow(() -> new RuntimeException("Unit representatives not found"));

        representatives.setCardPositionMemberMap(requestDTO.getCardPositionMemberMap());

        if (coverPhoto != null) {
            String coverPhotoUrl = s3Service.uploadFile(coverPhoto, "unit-photos", "cover-photo-" + unitId);
            representatives.setCoverPhotoUrl(coverPhotoUrl);
        }

        if (innerCoverPhoto != null) {
            String innerCoverPhotoUrl = s3Service.uploadFile(innerCoverPhoto, "unit-photos", "inner-photo-" + unitId);
            representatives.setInnerCoverPhotoUrl(innerCoverPhotoUrl);
        }

        representatives.setLastModifiedDate(LocalDateTime.now());
        return representativesRepository.save(representatives);
    }
}
