package cooperation.bus.domain.service;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.entity.Area;
import cooperation.bus.domain.entity.Member;
import cooperation.bus.domain.repository.AreaRepository;
import cooperation.bus.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AreaService {

    private final AreaRepository areaRepository;
    private final MemberRepository memberRepository;

    //findMember을 통해서 원하는 값을 얻어내고 거기서 area값을 얻고 areaDto값을 변동해하고 반환

    public void areaSave(AreaDto areaDto,String memberId){

        Member findMember = findMember(memberId);
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

    private Member findMember(String memberId) {
        Member findMember = memberRepository.findByLoginId(memberId).get();
        return findMember;
    }

    public String findStopId(String loginId){
        Member member = findMember(loginId);
        Area byMember = areaRepository.findByMember(member);
        if(byMember==null) {
            return null;
        }
        return byMember.getBusStopId();
    }

    public String findStationId(String loginId){
        Member member = findMember(loginId);
        Area byMember = areaRepository.findByMember(member);
        if(byMember==null){
            return null;
        }
        return byMember.getBusStationId();
    }

    public AreaDto findArea(String loginId){
        Member member = findMember(loginId);
        Area area = areaRepository.findByMember(member);
        log.info("777={}",area.getBusStationId());
        AreaDto areaDto = area.toAreaDto(area);
        log.info("888={}",areaDto.getBusStationId());
        return areaDto;
    }


}
