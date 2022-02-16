package com.studyolle.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component //spring 자동주입은 빈과 빈끼리만 가능(빈이 아닌경우 수동주입 해줘야 함)
@RequiredArgsConstructor //private final에 해당하는 멤버변수를 인자로 하는 생성자 생성
//스프링 4.2부터 어떤 빈이 생성자가 하나만 있고, 해당 생성자의 파라미터가 빈으로 등록되어 있다면 자동으로 빈 주입 해줌(생성자 @Autowired 생략 가능)
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) errors;
        if (accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()}, "이미 사용중인 이메일입니다.");
        }
        
        if (accountRepository.existsByNickname(signUpForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nick", new Object[]{signUpForm.getEmail()}, "이미 사용중인 닉네임입니다.");
        }
    }
}
