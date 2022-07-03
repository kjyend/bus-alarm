package cooperation.bus.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Area {//일단은 노선 id, 노선 번호,내려야할 역,
    @Id @GeneratedValue
    private Long id;
    private String busStopName;//내릴 버스 정류소 이름
    private String busStationId;//정류소id
    private String busStationName;//정류소 이름

    public Area() {
    }

    //x,y를 넣고 정류소Id를 뺀다. 정류소Id를 넣고 노선목록과, 노선Id를 뺀다
}
