package com.stjude.directory.dto;

import com.stjude.directory.enums.Unit;
import com.stjude.directory.model.Couple;
import com.stjude.directory.model.Family;
import com.stjude.directory.model.Member;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class FamilyResponseDTO {
    private String id;
    private String address;
    private String photoUrl; // URL of the family photo
    private List<MemberResponseDTO> familyMembers;
    private List<Couple> couples;
    private String houseName;
    private Unit unit;

    public FamilyResponseDTO(Family family, List<Member> members) {
        this.id = family.getId();
        this.address = family.getAddress();
        this.photoUrl = family.getPhotoUrl();
        this.houseName = family.getHouseName();
        this.familyMembers = members
                .stream()
                .map(MemberResponseDTO::new)
                .toList();
        this.couples = getCouples(members, family.getAnniversaryDates());
        this.unit = family.getUnit();
    }

    private List<Couple> getCouples(List<Member> members, Map<Short, Date> anniversaryDates) {
        if (CollectionUtils.isEmpty(anniversaryDates)) {
            return List.of();
        }
        // Group members by coupleNo for faster access
        Map<Short, List<String>> membersByCoupleNo = members.stream().filter(member -> Objects.nonNull(member.getCoupleNo()))
                .collect(Collectors.groupingBy(
                        Member::getCoupleNo,
                        Collectors.mapping(Member::getName, Collectors.toList())
                ));

        return anniversaryDates.entrySet().stream()
                .map(entry -> {
                    List<String> names = membersByCoupleNo.get(entry.getKey());
                    if (names != null && names.size() >= 2) {
                        return new Couple(names.get(0), names.get(1), entry.getValue());
                    }
                    return null; // Handle cases where names are missing or invalid
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
