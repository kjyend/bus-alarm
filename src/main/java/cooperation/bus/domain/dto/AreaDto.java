package cooperation.bus.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AreaDto {

    private String busStopName;//내릴 버스 정류소 이름
    private String busStationId;//정류소id
    private String busStationName;//정류소 이름

}
