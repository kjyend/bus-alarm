package cooperation.bus.domain.entity;

import cooperation.bus.domain.dto.MemberDto;
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
    private String loginId;
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
