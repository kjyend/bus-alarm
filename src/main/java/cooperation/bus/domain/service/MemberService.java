package cooperation.bus.domain.service;

import cooperation.bus.domain.dto.BusDto;
import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.entity.Member;
import cooperation.bus.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void save(MemberDto memberDto){

        Member member = memberDto.toMemberEntity(memberDto);
        memberRepository.save(member);
    }


    public void findLogin(String loginId){
        memberRepository.findByLoginId(loginId);
    }


}
