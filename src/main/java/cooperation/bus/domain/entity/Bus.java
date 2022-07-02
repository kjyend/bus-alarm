package cooperation.bus.domain.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Builder
public class Bus {// 버스 번호,노선id를 넣고,버스 위치
    @Id @GeneratedValue
    private Long id;
    private Long busNumber;//버스 번호(노선 번호)
    private String busId;//버스 노선id
    private String busLive;//현재 버스위치


    public Bus() {

    }
}
