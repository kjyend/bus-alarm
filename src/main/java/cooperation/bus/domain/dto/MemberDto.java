package cooperation.bus.domain.dto;

import lombok.Getter;

@Getter
public class MemberDto {
    private String loginId;
    private String password;
    private String name;
    private String dob;

    public MemberDto() {
    }

    public MemberDto(String loginId, String password, String name, String dob) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.dob = dob;
    }
}
