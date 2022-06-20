package cooperation.bus.web.controller;

import cooperation.bus.domain.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm(@RequestParam("loginId") String loginId, @RequestParam("password") String password, Model model){
        model.addAttribute("loginId", loginId);
        model.addAttribute("password",password);
        return "member/login";
    }


}
