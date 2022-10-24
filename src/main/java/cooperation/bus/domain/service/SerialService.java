package cooperation.bus.domain.service;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.dto.BusDto;
import cooperation.bus.domain.service.serialout.SerialWrite;
import gnu.io.*;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

//자바빈으로 등록해줘야한다.
public class SerialService {

    public void connect(String port,String busNumber,String startTime,String endTime) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException{
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
            int noBus = Integer.parseInt(busNumber);
            int start = Integer.parseInt(startTime);
            int end = Integer.parseInt(endTime);

            JSONObject data = new JSONObject();
            data.put("BusNumber",noBus);//나중에 호출해서 넣자.
            data.put("LeftBusTime", start);
            data.put("ArriveTime", end);

            OutputStream out = serialPort.getOutputStream();
            out.write(data.toString().getBytes());
            new Thread(new SerialWrite(out)).start();

        }
    }
}
