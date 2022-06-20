package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.entity.Member;
import cooperation.bus.web.argumentresolver.Login;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

//    @GetMapping("/")
//    public String home(@Login Member loginMember, Model model){
//        if(loginMember!=null){
//            return "Home";
//        }
//        model.addAttribute("member",loginMember);
//        return "LoginHome";
//    }
    @GetMapping("/")
    public String home(@Login Member loginMember, Model model){
        if(loginMember==null){
            return "Home";
        }
        model.addAttribute("member",loginMember);
        return "LoginHome";

    }

}
