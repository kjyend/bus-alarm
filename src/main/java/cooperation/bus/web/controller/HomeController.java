package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.AreaDto;
import cooperation.bus.domain.dto.BusDto;
import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.service.BusService;
import cooperation.bus.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final BusService busService;

    @GetMapping("/")
    public String homeForm(@Login MemberDto loginMember, BusDto busDto, Model model) {
        if (loginMember == null) {
            return "Home";
        }
        model.addAttribute("member", loginMember);
        model.addAttribute("bus", busDto);
        return "LoginHome";
    }

    @PostMapping("/")
    public String homeData(BusDto busDto) {//나중에 값넣고 null처링해아한다.
        if (busDto == null) {
            return "LoginHome";
        }
        //save로 저장해야한다.
        return "redirect:setting";
    }

}
