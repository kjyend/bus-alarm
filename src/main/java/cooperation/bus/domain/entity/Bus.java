package cooperation.bus.domain.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bus {

    @Id @GeneratedValue
    @Column(name = "bus_id")
    private Long id;

    private String busNumber;//버스 번호(노선 번호)
    private String busNodeId;//버스 노선id
    private String busArea;//버스 지역

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Bus(Long id, String busNumber, String busNodeId, String busArea, Member member) {
        this.id = id;
        this.busNumber = busNumber;
        this.busNodeId = busNodeId;
        this.busArea = busArea;
        this.member = member;
    }

    public void CreateBusMember(Member member){
         this.member=member;
    }

    public void UpdateBus(String busNumber, String busNodeId, String busArea) {
        this.busNumber = busNumber;
        this.busNodeId = busNodeId;
        this.busArea = busArea;
    }
}
