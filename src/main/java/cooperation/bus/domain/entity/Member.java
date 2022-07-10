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

    @OneToOne
    @JoinColumn(name = "b_id")
    private Bus bus;


}
