package cooperation.bus.domain.service;

import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public MemberDto login(String loginId, String password){
        return (MemberDto) memberRepository.findByLoginIdLike(loginId).stream().filter(m->m.getPassword().equals(password));
    }
}
