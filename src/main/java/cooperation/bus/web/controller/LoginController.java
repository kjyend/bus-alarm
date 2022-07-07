package cooperation.bus.web.controller;

import cooperation.bus.domain.dto.MemberDto;
import cooperation.bus.domain.service.LoginService;
import cooperation.bus.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private final LoginService loginService;

    @GetMapping("/signup")
    public String signupForm(MemberDto memberDto,Model model){
        model.addAttribute("member",memberDto);
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated MemberDto memberDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "member/signup";
        }
        memberService.save(memberDto);
        return "redirect:";
    }

    @GetMapping("/login")
    public String loginForm(MemberDto memberDto,Model model){
        model.addAttribute("member",memberDto);
        return "member/login";
    }

    @PostMapping("/login")
    public String login(@Validated MemberDto memberDto, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectULR, HttpServletRequest request){// dto로 바꿔야한다.
        if(bindingResult.hasErrors()){
            return "member/login";
        }
        MemberDto loginMember=loginService.login(memberDto);

        if(loginMember==null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "member/login";
        }
        HttpSession session=request.getSession();
        session.setAttribute("loginMember",loginMember);
        return "redirect:"+redirectULR;
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
