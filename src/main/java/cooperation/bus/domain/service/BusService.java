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

    public void busSave(BusDto busDto,String memberId){
        Bus bus = busDto.toBusEntity(busDto);

        Optional<Member> findMember = memberRepository.findByLoginId(memberId);
        bus.putMember(findMember);

        busRepository.save(bus);
    }


}
