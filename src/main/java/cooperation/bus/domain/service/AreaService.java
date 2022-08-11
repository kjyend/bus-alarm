package cooperation.bus.domain.service;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.entity.Area;
import cooperation.bus.domain.entity.Member;
import cooperation.bus.domain.repository.AreaRepository;
import cooperation.bus.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AreaService {

    private final AreaRepository areaRepository;
    private final MemberRepository memberRepository;

    public void areaSave(AreaDto areaDto,String memberId){

        Member findMember = memberRepository.findByLoginId(memberId).get();
        Area areaMember = areaRepository.findByMember(findMember);

        if(areaMember!=null) {

            areaMember.UpdateArea(areaDto.getBusStationId(),areaDto.getBusStationName(),areaDto.getBusStopId(),areaDto.getBusStopName());

            areaRepository.save(areaMember);

        }else {

            Area area = areaDto.toAreaEntity(areaDto);

            area.CreateAreaMember(findMember);

            areaRepository.save(area);

        }

    }

}
