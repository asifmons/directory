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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UnitRepresentativesServiceImpl implements UnitRepresentativesService {

    private static final Logger log = LoggerFactory.getLogger(UnitRepresentativesServiceImpl.class);

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
        log.info("Creating unit representatives template: name={}, cards={}",
                templateDTO != null ? templateDTO.getName() : null,
                templateDTO != null && templateDTO.getCards() != null ? templateDTO.getCards().size() : null);

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

        UnitRepresentativesTemplate saved = templateRepository.save(template);
        log.info("Created unit representatives template: id={}, name={}, cards={}", saved.getId(), saved.getName(),
                saved.getCards() != null ? saved.getCards().size() : 0);
        return saved;
    }

    @Override
    public List<UnitRepresentativesTemplate> getAllTemplates() {
        List<UnitRepresentativesTemplate> templates = templateRepository.findAll();
        log.debug("Fetched unit representatives templates: count={}", templates != null ? templates.size() : 0);
        return templates;
    }

    @Override
    public UnitRepresentatives assignRepresentatives(AssignRepresentativesRequestDTO requestDTO, MultipartFile coverPhoto, MultipartFile innerCoverPhoto) {
        log.info("Assigning unit representatives: unitId={}, templateId={}, hasCoverPhoto={}, hasInnerCoverPhoto={}",
                requestDTO != null ? requestDTO.getUnitId() : null,
                requestDTO != null ? requestDTO.getTemplateId() : null,
                coverPhoto != null,
                innerCoverPhoto != null);

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
            log.debug("Uploaded cover photo for unitId={}: url={}", requestDTO.getUnitId(), coverPhotoUrl);
        }

        if (innerCoverPhoto != null) {
            String innerCoverPhotoUrl = s3Service.uploadFile(innerCoverPhoto, "unit-photos", "inner-photo-" + requestDTO.getUnitId());
            representatives.setInnerCoverPhotoUrl(innerCoverPhotoUrl);
            log.debug("Uploaded inner cover photo for unitId={}: url={}", requestDTO.getUnitId(), innerCoverPhotoUrl);
        }

        representatives.setLastModifiedDate(LocalDateTime.now());
        UnitRepresentatives saved = representativesRepository.save(representatives);
        log.info("Assigned unit representatives saved: unitId={}, templateId={}", saved.getUnitId(), saved.getTemplateId());
        return saved;
    }

    @Override
    public UnitRepresentativesDetailDTO getRepresentativesByUnit(String unitId) {
        log.debug("Fetching unit representatives details: unitId={}", unitId);
        UnitRepresentatives representatives = representativesRepository.findByUnitId(unitId)
                .orElseThrow(() -> new RuntimeException("Unit representatives not found"));
        UnitRepresentativesTemplate template = templateRepository.findById(representatives.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        log.debug("Building unit representatives detail: unitId={}, templateId={}, cards={}",
                unitId,
                template.getId(),
                template.getCards() != null ? template.getCards().size() : 0);

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
        log.info("Updating unit representatives template: templateId={}, name={}, cards={}",
                templateId,
                templateDTO != null ? templateDTO.getName() : null,
                templateDTO != null && templateDTO.getCards() != null ? templateDTO.getCards().size() : null);

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

        UnitRepresentativesTemplate saved = templateRepository.save(template);
        log.info("Updated unit representatives template saved: templateId={}, name={}, cards={}", saved.getId(), saved.getName(),
                saved.getCards() != null ? saved.getCards().size() : 0);
        return saved;
    }

    @Override
    public UnitRepresentatives updateAssignedRepresentatives(String unitId, AssignRepresentativesRequestDTO requestDTO, MultipartFile coverPhoto, MultipartFile innerCoverPhoto) {
        log.info("Updating assigned unit representatives: unitId={}, hasCoverPhoto={}, hasInnerCoverPhoto={}",
                unitId,
                coverPhoto != null,
                innerCoverPhoto != null);

        UnitRepresentatives representatives = representativesRepository.findByUnitId(unitId)
                .orElseThrow(() -> new RuntimeException("Unit representatives not found"));

        representatives.setCardPositionMemberMap(requestDTO.getCardPositionMemberMap());

        if (coverPhoto != null) {
            String coverPhotoUrl = s3Service.uploadFile(coverPhoto, "unit-photos", "cover-photo-" + unitId);
            representatives.setCoverPhotoUrl(coverPhotoUrl);
            log.debug("Uploaded cover photo for unitId={}: url={}", unitId, coverPhotoUrl);
        }

        if (innerCoverPhoto != null) {
            String innerCoverPhotoUrl = s3Service.uploadFile(innerCoverPhoto, "unit-photos", "inner-photo-" + unitId);
            representatives.setInnerCoverPhotoUrl(innerCoverPhotoUrl);
            log.debug("Uploaded inner cover photo for unitId={}: url={}", unitId, innerCoverPhotoUrl);
        }

        representatives.setLastModifiedDate(LocalDateTime.now());
        UnitRepresentatives saved = representativesRepository.save(representatives);
        log.info("Updated assigned unit representatives saved: unitId={}, templateId={}", saved.getUnitId(), saved.getTemplateId());
        return saved;
    }
}
