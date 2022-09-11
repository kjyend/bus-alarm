package cooperation.bus.domain.entity;

import cooperation.bus.domain.dto.AreaDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Area {//일단은 노선 id, 노선 번호,내려야할 역,
    @Id @GeneratedValue
    private Long id;
    private String busStationName;//현재 정류소 이름
    private String busStationId;//현재 정류소id
    private String busStopName;//내릴 버스 정류소 이름
    private String busStopId;//내릴 버스 정류소id

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void CreateAreaMember(Member member){this.member=member;}

    public Area() {
    }

    public void UpdateArea(String busStationId,String busStationName,String busStopId,String busStopName){
        this.busStationId=busStationId;
        this.busStationName=busStationName;
        this.busStopId=busStopId;
        this.busStopName=busStopName;
    }

    public AreaDto toAreaDto(Area area){
        return AreaDto.builder()
                .busStationId(area.getBusStationId())
                .busStationName(area.getBusStationName())
                .busStopName(area.getBusStopName())
                .busStopId(area.getBusStopId())
                .member(area.getMember()).build();
    }

}
