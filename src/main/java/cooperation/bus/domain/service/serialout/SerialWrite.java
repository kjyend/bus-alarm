package cooperation.bus.domain.service.serialout;

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

        int c = 0;
        try
        {

            while ((c = System.in.read()) > -1)
            {
                out.write(c);
            }
        } catch (IOException e) {e.printStackTrace();}
    }
}