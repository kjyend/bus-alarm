package cooperation.bus.domain.dto;

import cooperation.bus.domain.entity.Area;
import cooperation.bus.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AreaDto {

    private String busStationId;//정류소id
    private String busStationName;//현재 정류소 이름
    private String busStopName;//내릴 버스 정류소 이름
    private String busStopId;//내릴 버스 정류소id
    private Member member;

    public AreaDto toAreaDto(Area area){
        return AreaDto.builder()
                .busStationId(area.getBusStationId())
                .busStopName(area.getBusStopName())
                .busStopName(area.getBusStopName())
                .busStopId(area.getBusStopId())
                .member(area.getMember()).build();
    }
    public Area toAreaEntity(AreaDto areaDto){
        return Area.builder()
                .busStationId(areaDto.getBusStationId())
                .busStationName(areaDto.getBusStationName())
                .busStopName(areaDto.getBusStopName())
                .busStopId(areaDto.getBusStopId())
                .member(areaDto.getMember()).build();
    }
}
