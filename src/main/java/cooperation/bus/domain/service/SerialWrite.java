package cooperation.bus.domain.service;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;


public class SerialWrite implements Runnable {

    private final OutputStream out;

    public SerialWrite(OutputStream out) {
        this.out = out;
    }

    @Override
    public void run()
    {
        JSONObject data=new JSONObject();

//        데이터를 json형식으로 보내야한다. json형식으로 만들어야하는데 데이터를 받아야하는 부분에서 문제
//        아래 형태로 바로 보내면 된다.
//        data.put();
//        data.put();
//        data.put();

        int c = 0;
        try
        {
            while ((data.size() = System.in.read()) > -1)
            {
                out.write(data);
            }
        } catch (IOException e) {e.printStackTrace();}
    }
}