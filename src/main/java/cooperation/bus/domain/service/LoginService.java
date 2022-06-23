package cooperation.bus.domain.service;

import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.entity.Member;
import cooperation.bus.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public MemberDto login(MemberDto memberDto) {
        Member member = memberDto.toMemberEntity(memberDto);
        Member member1 = memberRepository.findByLoginId(member.getLoginId()).filter(m -> m.getPassword().equals(member.getPassword())).orElse(null);
        if(member1!=null) {
            return memberDto.toMemberDto(member1);// 일단 되기는 한다 null일때 toMemberDto에서 못받는다. 한번 봐야겠다.
        }else{
            return null;
        }
    }
}
