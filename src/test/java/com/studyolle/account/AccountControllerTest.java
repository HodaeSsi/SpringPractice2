package com.studyolle.account;

import com.studyolle.domain.Account;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@AutoConfigureWebTestClient
class AccountControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private AccountRepository accountRepository;

    @MockBean
    JavaMailSender javaMailSender;

    @DisplayName("회원 가입 화면 보이는지 테스트")
    @Test
    void signUpForm() throws Exception {
            mockMvc.perform(get("/sign-up"))
//                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("account/sign-up"))
                    .andExpect(model().attributeExists("signUpForm"));
    }

    @DisplayName("회원 가입 처리 - 입력값 오류")
    @Test
    void signUpSubmit_with_wrong_input() throws Exception {
    mockMvc.perform(post("/sign-up")
            .param("nickname", "hodaessi")
            .param("email", "email..")
            .param("password", "12345")
            .with(csrf())) //스프링 시큐리티: "폼 데이터와 csrf 토큰"
            .andExpect(status().isOk())
            .andExpect(view().name("account/sign-up"));
    }

    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    void signUpSubmit_with_correct_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("nickname", "hodaessi")
                .param("email", "zz4471@naver.com")
                .param("password", "12345678")
                .with(csrf())) //스프링 시큐리티: "폼 데이터와 csrf 토큰"
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        Account account = accountRepository.findByEmail("zz4471@naver.com");

        assertNotNull(account);
        assertNotEquals(account.getPassword(),"12345678");
        //저장된 해시값이 평문과 같지 않음을 검증하지 않고,
        //평문 + salt가 저장된 해시값과 같은지 직접 비교는 해볼 수 없나???(스프링시큐리티 메서드 직접 가져다가)
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }
}