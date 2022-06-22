package cooperation.bus.domain.service;

import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.entity.Member;
import cooperation.bus.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public MemberDto login(MemberDto memberDto){//일단 에러가 난다. stream에러 같다는 생각 한번봐야할듯 일단 이번 목표
        Member member = memberDto.toMemberEntity(memberDto);
        return (MemberDto) memberRepository.findByLoginId(member.getLoginId()).stream().filter(Member->member.getPassword().equals(member.getPassword()));
    }
}
