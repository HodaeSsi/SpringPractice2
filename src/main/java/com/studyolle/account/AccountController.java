package com.studyolle.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
//        model.addAttribute("signUpForm", new SignUpForm());
        model.addAttribute(new SignUpForm()); //attributeName 생략가능 : 캐멀케이스를 준수한 클래스명을 사용할 때
        return "account/sign-up";
    }
}
