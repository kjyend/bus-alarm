package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.MemberDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/signup")
    public String signupForm(MemberDto memberDto,Model model){
        model.addAttribute("member",memberDto);
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signup(){

        return "redirect:";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "member/login";
    }

    @PostMapping("/login")
    public String login(){
        return "redirect:";
    }

    @PostMapping("logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }

}
