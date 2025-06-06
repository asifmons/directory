package com.stjude.directory.service;

import com.stjude.directory.model.Member;
import com.stjude.directory.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getMembersByFamilyId(String familyId) {
        return memberRepository.findByFamilyId(familyId);
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public List<Member> saveAllMembers(List<Member> members) {
        return memberRepository.saveAll(members);
    }

    public void deleteMember(String memberId) {
        memberRepository.deleteById(memberId);
    }

    public Member getMemberById(String memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return member.orElse(null);
    }

    /**
     * Retrieves a member by their ID and family ID.
     *
     * @param familyId the ID of the family
     * @param memberId the ID of the member
     * @return the member if found
     * @throws RuntimeException if the member is not found
     */
    public Member getMemberByFamilyIdAndMemberId(String familyId, String memberId) {
        return memberRepository.findMemberByFamilyIdAndMemberId(memberId, familyId)
                .orElseThrow(() -> new RuntimeException("Family member not found"));
    }

    public List<Member> getMembersByIds(List<String> memberIds) {
        return memberRepository.findAllById(memberIds);
    }
}
