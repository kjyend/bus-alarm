package cooperation.bus.domain.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id @GeneratedValue
    private Long id;
    @NotNull
    private String loginId;
    @NotNull
    private String password;
    private String name;
    private String dob;

    @OneToOne(mappedBy = "member",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Bus bus;
}
