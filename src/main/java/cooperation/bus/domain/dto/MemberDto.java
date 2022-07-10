package cooperation.bus.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cooperation.bus.domain.entity.Bus;
import cooperation.bus.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberDto {

    private String loginId;
    private String password;
    private String name;
    private String dob;
    private Bus bus;


    public MemberDto toMemberDto(Member member) {
        return MemberDto.builder()
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .name(member.getName())
                .dob(member.getDob())
                .bus(member.getBus()).build();
    }

    public Member toMemberEntity(MemberDto memberDto){
        return Member.builder()
                .loginId(memberDto.getLoginId())
                .password(memberDto.getPassword())
                .name(memberDto.getName())
                .dob(memberDto.getDob())
                .bus(memberDto.getBus()).build();
    }

}
