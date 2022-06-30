package cooperation.bus.domain.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Area {//일단은 노선 id, 노선 번호,내려야할 역,
    @Id @GeneratedValue
    private Long id;
    private String tx;
    private String ty;
    private String busStop;

    //x,y를 넣고 정류소Id를 뺀다. 정류소Id를 넣고 노선목록과, 노선Id를 뺀다
}
