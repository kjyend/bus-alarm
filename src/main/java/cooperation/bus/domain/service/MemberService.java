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
        
    //일단 1번 member에 bus값을 어떻게 채울것인가 다음으로 그 값을 무엇으로 할것인가? 
    //2. findLogin을 살펴봐서 true면 bus랑 같이 저장하고 없으면 같이 저장하지 않는다.
    //3.change을 메서드로 만들어서 busNodeId를 업데이트 해줘야할듯 setter를 안쓰고
    public void findLogin(String loginId){
        memberRepository.findByLoginId(loginId);
    }
}
