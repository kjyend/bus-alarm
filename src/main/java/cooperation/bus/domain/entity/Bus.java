package cooperation.bus.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bus {// 버스 번호,노선id를 넣고,버스 위치

    @Id @GeneratedValue
    @Column(name = "bus_id")
    private Long id;

    private String busNumber;//버스 번호(노선 번호)
    private String busNodeId;//버스 노선id
    private String busArea;//버스 지역

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void putMember(Member member){
         this.member=member;
    }

}
