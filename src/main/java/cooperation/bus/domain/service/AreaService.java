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


}
