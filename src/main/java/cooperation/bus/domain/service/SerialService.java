package cooperation.bus.domain.service;

import gnu.io.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class SerialService {

    public void connect(String port) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException{
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
            OutputStream out = serialPort.getOutputStream();
            new Thread(new SerialWrite(out)).start();

        }
    }
}
