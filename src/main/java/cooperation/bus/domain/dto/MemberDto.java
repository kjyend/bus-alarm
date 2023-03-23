package cooperation.bus.domain.dto;

import cooperation.bus.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
public class MemberDto {

    @NotBlank
    @Email(message = "이메일 형식을 맞춰주세요")
    private String loginId;
    @NotBlank
    @Size(min = 2,max=20, message = "비밀번호를 2-20자 사이로 입력해주세요")
    private String password;
    private String name;
    private String dob;


    public MemberDto toMemberDto(Member member) {
        return MemberDto.builder()
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .name(member.getName())
                .dob(member.getDob())
                .build();
    }

    public Member toMemberEntity(MemberDto memberDto){
        return Member.builder()
                .loginId(memberDto.getLoginId())
                .password(memberDto.getPassword())
                .name(memberDto.getName())
                .dob(memberDto.getDob())
                .build();
    }

}
