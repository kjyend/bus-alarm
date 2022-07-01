package cooperation.bus.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BusDto {

    private String busNumber;//버스 번호(노선 번호)
    private String busId;//버스 노선id
    private String busLive;//현재 버스위치

}
