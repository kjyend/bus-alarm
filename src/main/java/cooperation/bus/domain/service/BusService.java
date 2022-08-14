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

        Member findMember = findMember(memberId);
        Bus busMember = busRepository.findByMember(findMember);

        if(busMember!=null){

            busMember.UpdateBus(busDto.getBusNumber(),busDto.getBusNodeId(),busDto.getBusArea());

            busRepository.save(busMember);
        }else{
            Bus bus = busDto.toBusEntity(busDto);

            bus.CreateBusMember(findMember);

            busRepository.save(bus);
        }

    }

    private Member findMember(String memberId) {
        Member findMember = memberRepository.findByLoginId(memberId).get();
        return findMember;
    }

    public String nodeFind(String memberId){
        Member member = findMember(memberId);
        Bus byMember = busRepository.findByMember(member);
        if (byMember==null){
            return null;
        }
        return byMember.getBusNodeId();
    }

}
