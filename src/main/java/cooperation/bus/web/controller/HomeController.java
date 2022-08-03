package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.BusDto;
import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.web.argumentresolver.Login;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/")
    public String homeForm(@Login MemberDto loginMember, BusDto busDto, Model model) {
        if (loginMember == null) {
            return "Home";
        }
        model.addAttribute("bus",busDto);
        return "LoginHome";
    }

    @GetMapping("/bus")
    public String busHomeForm(@Login MemberDto loginMember, BusDto busDto, Model model){
        if (loginMember == null) {
            return "Home";
        }
        model.addAttribute("bus",busDto);
        return "LoginHome";
    }

}
