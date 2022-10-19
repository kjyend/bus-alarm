package cooperation.bus;

import cooperation.bus.domain.service.SerialService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerialConfig {
    @Bean
    public SerialService serial(){
        return new SerialService();
    }
}
