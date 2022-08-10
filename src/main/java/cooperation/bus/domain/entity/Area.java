package cooperation.bus.domain.entity;

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
    private String busStationId;//정류소id
    private String busStationName;//정류소 이름
    private String busStopName;//내릴 버스 정류소 이름
    private String busStopId;//내릴 버스 정류소id

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void CreateAreaMember(Member member){this.member=member;}

    public Area() {
    }

}
