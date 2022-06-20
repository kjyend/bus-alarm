package cooperation.bus.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id @GeneratedValue
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private String dob;

}
