package cooperation.bus.domain.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Bus {
    @Id @GeneratedValue
    private Long id;
    private String busName;
    private String busPosition;
}
