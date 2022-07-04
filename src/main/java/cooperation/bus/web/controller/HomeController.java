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

import javax.servlet.http.HttpServletRequest;

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

}
