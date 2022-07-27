package cooperation.bus.domain.dto;

import cooperation.bus.domain.entity.Bus;
import cooperation.bus.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BusDto {

    private String busNumber;//버스 번호(노선 번호)
    private String busNodeId;//버스 노선id
    private String busArea;//현재 버스위치
    private Member member;

    public BusDto toBusDto(Bus bus){//하나씩 따로 바는것도 생각해야한다.
        return BusDto.builder()
                .busNumber(bus.getBusNumber())
                .busNodeId(bus.getBusNodeId())
                .busArea(bus.getBusArea())
                .member(bus.getMember())
                .build();
    }


    public Bus toBusEntity(BusDto busDto){
        return Bus.builder()
                .busNumber(busDto.getBusNumber())
                .busNodeId(busDto.getBusNodeId())
                .busArea(busDto.getBusArea())
                .member(busDto.getMember())
                .build();

    }

}
