package cooperation.bus.domain.service;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.entity.Area;
import cooperation.bus.domain.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AreaService {

    private final AreaRepository areaRepository;

    public void areaSave(AreaDto areaDto){
        Area area = areaDto.toAreaEntity(areaDto);
        areaRepository.save(area);
    }

}
