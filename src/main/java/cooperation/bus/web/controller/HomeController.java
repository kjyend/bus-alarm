package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.web.argumentresolver.Login;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String homeForm(@Login MemberDto loginMember, AreaDto areaDto, Model model) {
        if (loginMember == null) {
            return "Home";
        }
        model.addAttribute("member", loginMember);
        model.addAttribute("area", areaDto);
        return "LoginHome";
    }

    @PostMapping("/")
    public String homeData(AreaDto areaDto, Model model) {//나중에 값넣고 null처링해아한다.
        if (areaDto == null) {
            return "LoginHome";
        }
        model.addAttribute("area", areaDto);
        return "redirect:";
    }

}
