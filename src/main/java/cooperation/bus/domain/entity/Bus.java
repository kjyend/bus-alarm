package cooperation.bus.domain.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Bus {//내려야할 역, 버스 번호,노선id를 넣고,버스 위치
    @Id @GeneratedValue
    private Long id;
    private String busName;
    private String busPosition;
}
