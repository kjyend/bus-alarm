package cooperation.bus.domain.service;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.dto.BusDto;
import cooperation.bus.domain.service.serialout.SerialWrite;
import gnu.io.*;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;

@Service
@Slf4j
public class SerialService {

    public void connect(String port, AreaDto areaDto, BusDto busDto,String startTime,String endTime) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException{
        CommPort commPort = null;
        SerialPort serialPort = null;
        CommPortIdentifier com = CommPortIdentifier.getPortIdentifier(port);
        if (com.isCurrentlyOwned()) {
            System.out.println("Error : " + port + "포트를 사용중입니다.");
        }
        else {
            commPort = com.open(this.getClass().getName(), 2000);
            if (commPort instanceof SerialPort)    //	commPort가 SerialPort로 사용할 수 있는지 확인
            {
                serialPort = (SerialPort) commPort; //시리얼 포트

                serialPort.setSerialPortParams(
                        9600,                        //	바운드레이트
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);    //	오류제어 비트
            }

            JSONObject data = new JSONObject();
            data.put("버스번호",busDto.getBusNumber());//나중에 호출해서 넣자.
            data.put("1번 정류소 이름",areaDto.getBusStationName());//나중에 호출해서 넣자.
            data.put("1번 정류소 도착 시간", startTime);
            data.put("2번 정류소 이름",areaDto.getBusStopName());//나중에 호출해서 넣자.
            data.put("2번 정류소 도착 시간", endTime);


            OutputStream out = serialPort.getOutputStream();
            out.write(data.toString().getBytes());
            log.info("out={}",out.getClass());
            log.info("out={}",out);
            log.info("out={}",out.toString());
            new Thread(new SerialWrite(out)).start();

        }
    }
}
