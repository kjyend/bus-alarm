package cooperation.bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BusApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusApplication.class, args);
//		try {
//			(new SerialService()).connect("COM5");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
