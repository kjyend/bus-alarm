package cooperation.bus.domain.dto;

import cooperation.bus.domain.entity.Bus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BusDto {//builder만들기 다시해야한다.ㄷ

    private String busNumber;//버스 번호(노선 번호)
    private String busId;//버스 노선id
    private String busArea;//현재 버스위치

    public BusDto(String busNumber, String busId, String busArea) {
        this.busNumber = busNumber;
        this.busId = busId;
        this.busArea = busArea;
    }

    public BusDto toBusDto(Bus bus){//하나씩 따로 바는것도 생각해야한다.
        return BusDto.builder()
                .busNumber(bus.getBusNumber())
                .busId(bus.getBusId())
                .busArea(bus.getBusArea()).build();
    }


    public Bus toBusEntity(BusDto busDto){
        return Bus.builder()
                .busNumber(busDto.busNumber)
                .busId(busDto.getBusId())
                .busArea(busDto.getBusArea()).build();

    }

}
