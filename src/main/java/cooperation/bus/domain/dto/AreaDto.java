package cooperation.bus.domain.dto;

import cooperation.bus.domain.entity.Area;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AreaDto {

    private String busStopName;//내릴 버스 정류소 이름
    private String busStationId;//정류소id
    private String busStationName;//정류소 이름

    public AreaDto toAreaDto(Area area){
        return AreaDto.builder()
                .busStopName(area.getBusStopName())
                .busStationId(area.getBusStationId())
                .busStopName(area.getBusStopName()).build();
    }
    public Area toAreaEntity(AreaDto areaDto){
        return Area.builder()
                .busStopName(areaDto.getBusStopName())
                .busStationId(areaDto.getBusStationId())
                .busStationName(areaDto.getBusStationName()).build();
    }
}
