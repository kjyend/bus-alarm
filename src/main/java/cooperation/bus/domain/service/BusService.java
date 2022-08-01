package cooperation.bus.domain.service;


import cooperation.bus.domain.dto.BusDto;
import cooperation.bus.domain.entity.Bus;
import cooperation.bus.domain.entity.Member;
import cooperation.bus.domain.repository.BusRepository;
import cooperation.bus.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;
    private final MemberRepository memberRepository;

    public void busSave(BusDto busDto,String memberId){

        Member findMember = memberRepository.findByLoginId(memberId).get();
        Bus busMember = busRepository.findByMember(findMember);

        //파일을 저장해야하는데 하나의 값으로 저장해야한다. member값으로 비교해서 없으면 새로 저장 잇으면 update해야한다.
        if(busMember!=null){

            busMember.UpdateBus(busDto.getBusNumber(),busDto.getBusNodeId(),busDto.getBusArea());

            busRepository.save(busMember);
        }else{
            Bus bus = busDto.toBusEntity(busDto);

            bus.CreateMember(findMember);

            busRepository.save(bus);
        }

    }
}
