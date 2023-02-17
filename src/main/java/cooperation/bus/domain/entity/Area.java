package cooperation.bus.domain.entity;

import cooperation.bus.domain.dto.AreaDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Area {
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

    @Builder
    public Area(Long id, String busStationName, String busStationId, String busStopName, String busStopId, Member member) {
        this.id = id;
        this.busStationName = busStationName;
        this.busStationId = busStationId;
        this.busStopName = busStopName;
        this.busStopId = busStopId;
        this.member = member;
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
