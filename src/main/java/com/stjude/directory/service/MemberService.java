package com.stjude.directory.service;

import com.stjude.directory.model.Member;
import com.stjude.directory.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
