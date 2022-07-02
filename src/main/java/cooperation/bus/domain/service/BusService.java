package cooperation.bus.domain.service;

import cooperation.bus.domain.dto.BusDto;
import cooperation.bus.domain.entity.Bus;
import cooperation.bus.domain.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;


}
