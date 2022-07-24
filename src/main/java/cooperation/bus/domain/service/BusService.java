package cooperation.bus.domain.service;


import cooperation.bus.domain.dto.BusDto;
import cooperation.bus.domain.entity.Bus;
import cooperation.bus.domain.entity.Member;
import cooperation.bus.domain.repository.BusRepository;
import cooperation.bus.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;
    private final MemberRepository memberRepository;

    public void busSave(BusDto busDto){
        Bus bus = busDto.toBusEntity(busDto);
        busRepository.save(bus);
    }
    public void findLogin(String loginId){//loginid을 확인한다.
        //확인하고 있으면 busdto에 채워넣어야한다.
        Optional<Member> memberLogin = memberRepository.findByLoginId(loginId);


    }

}
