package cooperation.bus.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bus {// 버스 번호,노선id를 넣고,버스 위치

    @Id @GeneratedValue
    private Long id;
    private String busNumber;//버스 번호(노선 번호)
    private String busId;//버스 노선id
    private String busArea;//버스 지역

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
