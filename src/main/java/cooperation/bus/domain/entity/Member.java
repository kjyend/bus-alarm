package cooperation.bus.domain.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue
    private Long id;
    @NotNull
    private String loginId;
    @NotNull
    private String password;
    private String name;
    private String dob;

    public Member(Long id, String loginId, String password, String name, String dob) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.dob = dob;
    }

}
